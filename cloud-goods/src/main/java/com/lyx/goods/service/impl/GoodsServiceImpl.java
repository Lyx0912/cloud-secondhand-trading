package com.lyx.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.common.base.entity.dto.GoodsDTO;
import com.lyx.common.base.entity.dto.GoodsEsDTO;
import com.lyx.common.base.entity.dto.StorageDTO;
import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.*;
import com.lyx.goods.entity.req.GoodsListPageReq;
import com.lyx.goods.entity.req.GoodsSaveReq;
import com.lyx.goods.entity.req.GoodsSaveTestReq;
import com.lyx.goods.entity.vo.GoodsReleaseVo;
import com.lyx.goods.entity.vo.GoodsVO;
import com.lyx.goods.feign.SearchElasticFeignService;
import com.lyx.goods.feign.StorageFeignService;
import com.lyx.goods.mapper.GoodsMapper;
import com.lyx.goods.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-31 11:16:28
 */
@Service
@Slf4j
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private GoodsImagesService imagesService;
    @Autowired
    private GoodsDetailsService detailsService;
    @Autowired
    private AuditService auditService;
    @Autowired
    private SearchElasticFeignService searchElasticFeignService;
    @Autowired
    private StorageFeignService storageFeignService;

    /**
     * 分页查询商品
     * @param req 查询条件对象
     */
    @Override
    public PageUtils<GoodsVO> listPage(GoodsListPageReq req) {
        // 构建分页对象 设置分页参数
        Page<GoodsVO> page = new Page<>(req.getPageNo(), req.getPageSize());
        // 构建查询条件
        QueryWrapper<Goods> wrapper = Wrappers.query();
        wrapper
                .like(StringUtils.isNotEmpty(req.getSeller()), "seller", req.getSeller())
                .like(StringUtils.isNotEmpty(req.getName()), "name", req.getName())
                .eq(req.getCategory_id()!=null?req.getCategory_id()!=0:false,
                        "cid", req.getCategory_id())
//                .eq("g.deleted", 0)
                .eq(req.getIsOnSell() != null, "is_on_sell", req.getIsOnSell());
        List<Goods> goods = this.baseMapper.selectList(wrapper);

        Page<GoodsVO> goodsVOPage = baseMapper.listPage(page, wrapper);
        // 过滤还未通过审核的商品
        List<GoodsVO> goodsVOS = goods.stream()
                .map(goods1 -> {
                    GoodsVO goodsVO = new GoodsVO();
                    BeanUtils.copyProperties(goods1,goodsVO);
                    return goodsVO;
                })
                .filter(good -> {
                    return auditService.getAuditById(good.getId())!=null;
                }).collect(Collectors.toList());
        page.setRecords(goodsVOS);
        PageUtils<GoodsVO> pageUtils = PageUtils.build(page);
        // 返回分页对象
        return pageUtils;
    }

    /**
     * 切换商品上架状态
     * @param goodsIds
     * @param isOnSell
     */
    @Transactional
    @Override
    public void changeIsOnSell(List<Long> goodsIds, Integer isOnSell) {
        LambdaUpdateWrapper<Goods> updateWrapper = Wrappers.lambdaUpdate();
        // 上架操作
        if (isOnSell == 1){
            List<GoodsEsDTO> goodsEsDTOs = new ArrayList<>();
            updateWrapper.set(Goods::getIsOnSell,1)
                    .in(Goods::getId,goodsIds);
            LambdaQueryWrapper<Goods> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Goods::getIsOnSell,0);
            // 过滤出需要上架的商品
            List<Goods> goods = this.list(wrapper).stream().filter(goods1 -> {
                for (Long goodsId : goodsIds) {
                    if (goods1.getId()==goodsId){
                        GoodsEsDTO goodsEsDTO = new GoodsEsDTO();
                        BeanUtils.copyProperties(goods1,goodsEsDTO);
                        goodsEsDTOs.add(goodsEsDTO);
                        return goods1.getId()==goodsId;
                    }
                }
                return false;
            }).collect(Collectors.toList());
            BeanUtils.copyProperties(goods,goodsEsDTOs);
            // 远程调用es 上架商品
            searchElasticFeignService.goodsStatusUp(goodsEsDTOs);
        // 下架操作
        }else {
            updateWrapper.set(Goods::getIsOnSell,0)
                    .in(Goods::getId,goodsIds);
            // 远程调用es 下架商品
            searchElasticFeignService.goodsDelete(goodsIds);
        }
        this.update(updateWrapper);
    }

    /**
     * 查询商品详情
     *
     * @param id
     */
    @Override
    public GoodsVO getGoodsVOById(Long id) {
        // 查询VO
        GoodsVO goodsVO = baseMapper.getGoodsVOById(id);
        // 递归查找父分类
        goodsVO.setCategoryPath(categoryService.findParentCategory(goodsVO.getCid()));
        // 设置商品图片
        List<GoodsImages> images = imagesService.lambdaQuery().eq(GoodsImages::getGoodsId, id).orderByAsc(GoodsImages::getCreateTime).select().list();
        goodsVO.setImages(images);
        // 设置商品详情
        GoodsDetails goodsDetails = detailsService.lambdaQuery().eq(GoodsDetails::getGoodsId, id).one();
        goodsVO.setDetails(goodsDetails);
        // 远程调用查询库存
        Integer residue = storageFeignService.residue(id);
        if (residue!=null){
            goodsVO.setTotal(residue);
            log.info("residue{}",residue);
        }else {
            goodsVO.setTotal(0);
        }
        return goodsVO;
    }

    /**
     * 查询商品详情
     *
     * @param id
     */
    @Override
    public GoodsVO getGoodsVOFeignById(Long id) {
        // 查询VO
        GoodsVO goodsVO = baseMapper.getGoodsVOById(id);
        // 递归查找父分类
        goodsVO.setCategoryPath(categoryService.findParentCategory(goodsVO.getCid()));
        // 设置商品图片
        List<GoodsImages> images = imagesService.lambdaQuery().eq(GoodsImages::getGoodsId, id).orderByAsc(GoodsImages::getCreateTime).select().list();
        goodsVO.setImages(images);
        // 设置商品详情
        GoodsDetails goodsDetails = detailsService.lambdaQuery().eq(GoodsDetails::getGoodsId, id).one();
        goodsVO.setDetails(goodsDetails);
        return goodsVO;
    }

    @Override
    public GoodsDTO getGoodsDTOById(Long id) {
        GoodsVO goodsVO = getGoodsVOById(id);
        log.info("goodsVO{}",goodsVO);
        GoodsDTO goodsDTO = new GoodsDTO();
        BeanUtils.copyProperties(goodsVO,goodsDTO);
        goodsDTO.setCategoryName(categoryService.getById(goodsVO.getCid()).getName());
        log.info("goodsDTO{}",goodsDTO);
        return goodsDTO;
    }

    /**
     * 更新商品详情
     *
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGoodsInfo(GoodsSaveReq req) {
        // 先拿到商品图片集合,拿到所有没id的对象进行入库操作。
        List<GoodsImages> images = req.getImages();
        List<GoodsImages> newImages = images.stream().filter(item -> item.getId() == null).collect(Collectors.toList());
        // 批量插入
        imagesService.saveBatch(newImages);
        // 收集id
        images.addAll(newImages);
        List<Long> allId = images.stream().filter(item -> item.getId() != null).map(item -> item.getId()).collect(Collectors.toList());
        // 从db中删除不存在的图片(前端执行了移除操作后列表中就不存在了)
        imagesService.lambdaUpdate().eq(GoodsImages::getGoodsId,req.getId()).notIn(!CollectionUtils.isEmpty(allId),GoodsImages::getId,allId).remove();


        // 更新商品详情
        detailsService.updateById(req.getDetails());
        // 更新商品信息
        Goods goods = new Goods();
        BeanUtils.copyProperties(req,goods);
        goods.setCid(req.getCategoryPath()[req.getCategoryPath().length-1]);
        goods.setUpdateTime(LocalDateTime.now());
        updateById(goods);
        // 更新elasticsearch
        if (req.getIsOnSell() == 1){
            ArrayList<GoodsEsDTO> goodsEsDTOS = new ArrayList<>();
            GoodsEsDTO goodsEsDTO = new GoodsEsDTO();
            BeanUtils.copyProperties(goods,goodsEsDTO);
            goodsEsDTOS.add(goodsEsDTO);
            searchElasticFeignService.goodsStatusUp(goodsEsDTOS);
        }
    }

    /**
     * 发布商品
     * @param req
     */
    @Transactional
    @Override
    public void saveGoodsInfo(GoodsSaveTestReq req) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(req,goods);
        goods.setViewCount(0);
        goods.setCreateTime(LocalDateTime.now());
        goods.setUpdateTime(LocalDateTime.now());
        goods.setDeleted(0);
        goods.setCid(req.getCategoryPath()[req.getCategoryPath().length-1]);
        goods.setIsOnSell(0);
        this.save(goods);
        // 加入商品的审核状态
        Audit audit = new Audit();
        audit.setDeleted(0);
        audit.setGoodsId(goods.getId());
        audit.setState(0L);
        auditService.save(audit);
        // 上传图片集
        List<GoodsImages> collect = req.getImages().stream().map(images -> {
            GoodsImages goodsImage = new GoodsImages();
            goodsImage.setGoodsId(goods.getId());
            goodsImage.setCreateTime(LocalDateTime.now());
            goodsImage.setUpdateTime(LocalDateTime.now());
            goodsImage.setUrl(images.getUrl());
            return goodsImage;
        }).collect(Collectors.toList());
        imagesService.saveBatch(collect);
        // 上传商品详情
//        if (req.getDetails()!=null){
//            detailsService.save(req.getDetails());
//        }
        // 上传库存
        StorageDTO storageDTO = new StorageDTO();
        storageDTO.setTotal(req.getTotal());
        storageDTO.setProductId(goods.getId());
        storageFeignService.save(storageDTO);
    }

    /**
     * 查询已上传的商品
     * @param req
     * @return
     */
    @Override
    public PageUtils<GoodsReleaseVo> releaseGoodslistPage(GoodsListPageReq req) {
        Page<GoodsReleaseVo> page = new Page<>(req.getPageNo(),req.getPageSize());
        LambdaQueryWrapper<Goods> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(req.getSeller_id()!=null,Goods::getSellerId,req.getSeller_id());
        List<Goods> goods = this.list(wrapper);
        List<GoodsReleaseVo> collect = goods.stream().map(good -> {
            GoodsReleaseVo goodsReleaseVo = new GoodsReleaseVo();
            LambdaQueryWrapper<Audit> auditWrapper = Wrappers.lambdaQuery();
            auditWrapper.eq(req.getSeller_id() != null, Audit::getGoodsId, good.getId());
            Audit audit = auditService.getOne(auditWrapper);
            BeanUtils.copyProperties(good, goodsReleaseVo);
            goodsReleaseVo.setAudit(audit);
            // 查询库存
            Integer residue = storageFeignService.residueGoodsId(good.getId());
            goodsReleaseVo.setTotal(residue);
            // 查询分类名称 和 三级分类
            Category category = categoryService.getById(good.getCid());
            goodsReleaseVo.setCategoryName(category.getName());
            return goodsReleaseVo;
        }).collect(Collectors.toList());
        page.setRecords(collect);
        PageUtils<GoodsReleaseVo> pageUtils = PageUtils.build(page);
        return pageUtils;
    }


}
