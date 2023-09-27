package com.lyx.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.common.base.entity.dto.*;
import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.*;
import com.lyx.goods.entity.req.GoodsListPageReq;
import com.lyx.goods.entity.req.GoodsSaveReq;
import com.lyx.goods.entity.req.GoodsSaveTestReq;
import com.lyx.goods.entity.vo.GoodsReleaseVo;
import com.lyx.goods.entity.vo.GoodsVO;
import com.lyx.goods.feign.MemberFeignService;
import com.lyx.goods.feign.SearchElasticFeignService;
import com.lyx.goods.feign.StorageFeignService;
import com.lyx.goods.mapper.GoodsMapper;
import com.lyx.goods.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
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
    @Autowired
    private MemberFeignService memberFeignService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ThreadPoolExecutor executor;


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
                        // 查询库存
                        Integer id = storageFeignService.residueGoodsId(goods1.getId());
                        // 上传库存到redis
                        redisTemplate.opsForValue().set("goods:"+goods1.getId(),id.toString());
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
    public GoodsVO getGoodsVOById(Long id) throws ExecutionException, InterruptedException {
        // 查询VO
        GoodsVO vo = new GoodsVO();
        CompletableFuture<GoodsVO> supplyAsync = CompletableFuture.supplyAsync(() -> {
            GoodsVO goodsVO = baseMapper.getGoodsVOById(id);
            return goodsVO;
        }, executor);
        CompletableFuture<Void> runAsync5 = supplyAsync.thenRunAsync(() -> {
            try {
                if (supplyAsync.get() != null) {

                    // 递归查找父分类
                    CompletableFuture<Void> runAsync = supplyAsync.thenAcceptAsync((goodsVO) -> {
                        try {
                            BeanUtils.copyProperties(supplyAsync.get(), vo);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        // 浏览量加一
                        baseMapper.updateViewCount(goodsVO.getId());
                        vo.setCategoryPath(categoryService.findParentCategory(goodsVO.getCid()));
                    }, executor);
                    // 设置商品图片
                    CompletableFuture<Void> runAsync1 = CompletableFuture.runAsync(() -> {
                        List<GoodsImages> images = imagesService.lambdaQuery().eq(GoodsImages::getGoodsId, id).orderByAsc(GoodsImages::getCreateTime).select().list();
                        vo.setImages(images);
                    }, executor);
                    // 设置商品详情
                    CompletableFuture<Void> runAsync2 = CompletableFuture.runAsync(() -> {
                        GoodsDetails goodsDetails = detailsService.lambdaQuery().eq(GoodsDetails::getGoodsId, id).one();
                        vo.setDetails(goodsDetails);
                    }, executor);
                    // 远程调用查询库存
                    CompletableFuture<Void> runAsync3 = CompletableFuture.runAsync(() -> {
                        Integer residueGoods = storageFeignService.residueGoodsId(id);
                        Integer residue = storageFeignService.residue(id);
                        if (residue != null) {
                            vo.setTotal(residue);
                        } else {
                            vo.setTotal(residueGoods == null ? 0 : residueGoods);
                        }
                    }, executor);
                    // 设置手机号码
                    CompletableFuture<Void> runAsync4 = CompletableFuture.runAsync(() -> {
                        Goods goods = getById(id);
                        String mobile = memberFeignService.getMemberMobile(Long.parseLong(goods.getSellerId() + ""));
                        vo.setMobile(mobile);
                    }, executor);
                    CompletableFuture.allOf(runAsync, runAsync1, runAsync2, runAsync3, runAsync4).get();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }, executor);
        CompletableFuture.allOf(runAsync5,supplyAsync).get();
        return vo;
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
        // 设置商品图片
        List<GoodsImages> images = imagesService.lambdaQuery().eq(GoodsImages::getGoodsId, id).orderByAsc(GoodsImages::getCreateTime).select().list();
        goodsVO.setImages(images);
        // 递归查找父分类
        goodsVO.setCategoryPath(categoryService.findParentCategory(goodsVO.getCid()));
        // 设置商品详情
        GoodsDetails goodsDetails = detailsService.lambdaQuery().eq(GoodsDetails::getGoodsId, id).one();
        goodsVO.setDetails(goodsDetails);
        return goodsVO;
    }

    @Override
    public GoodsDTO getGoodsDTOById(Long id) throws ExecutionException, InterruptedException {
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
//        detailsService.updateById(req.getDetails());
        // 更新商品信息
        Goods goods = new Goods();
        BeanUtils.copyProperties(req,goods);
        goods.setCid(req.getCategoryPath()[req.getCategoryPath().length-1]);
        goods.setUpdateTime(LocalDateTime.now());
        updateById(goods);
        if (req.getTotal()!=null){
            redisTemplate.opsForValue().set("goods:"+goods.getId(), req.getTotal().toString());
        }
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
        //
        GoodsDetails details = new GoodsDetails();
        details.setContent(("<h1>你好</h1>"));
        details.setGoodsId(goods.getId());
        details.setCreatedAt(LocalDateTime.now());
        details.setUpdatedAt(LocalDateTime.now());
        detailsService.save(details);
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
        Page<Goods> pageGoods = new Page<>(req.getPageNo(),req.getPageSize());
        // 构建查询商品的条件
        LambdaQueryWrapper<Goods> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(req.getSeller_id()!=null,Goods::getSellerId,req.getSeller_id());
        List<Goods> goods = this.page(pageGoods,wrapper).getRecords();
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
        pageUtils.setTotal(pageGoods.getTotal());
        pageUtils.setPageSize(pageGoods.getSize());
        return pageUtils;
    }

    /**
     * 用户分页查询商品列表
     */
    @Override
    public PageUtils<GoodsVO> listEsPage(GoodsListPageReq req) {
        Page<GoodsVO> page = new Page<>(req.getPageNo(),req.getPageSize());
        List<Long> categories = null;
        if (req.getCategory_id()!=0){
            // 查询一级分类下所有三级分类
            categories = baseMapper.category(req.getCategory_id());
        }
        // 远程调用es查询商品
        EsGoodsDTO esGoodsDTO = new EsGoodsDTO();
        esGoodsDTO.setCid(categories);
        esGoodsDTO.setName(req.getName());
        List<GoodsDTO> goodsDTOS = searchElasticFeignService.goodsEsList(esGoodsDTO);
        List<GoodsVO> collect = null;
        if (goodsDTOS!=null){
            collect = goodsDTOS.stream().map(goodsDTO -> {
                GoodsVO goodsVO = new GoodsVO();
                BeanUtils.copyProperties(goodsDTO, goodsVO);
                return goodsVO;
            }).collect(Collectors.toList());
        }
        page.setRecords(collect);
        PageUtils pageUtils = PageUtils.build(page);
        return pageUtils;
    }

    /**
     * 删除商品
     * @param ids
     */
    @Transactional
    @Override
    public void removeGoodsByIds(List<Long> ids) {
        this.removeByIds(ids);
        // 下架es商品
        searchElasticFeignService.goodsDelete(ids);
    }

    /**
     * 查询用户发布了多少条商品
     * @param memberId
     * @return
     */
    @Override
    public Integer memberIdCount(Long memberId) {
        // 查询用户发布了多少条商品
        LambdaQueryWrapper<Goods> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtils.isNotEmpty(memberId.toString()),Goods::getSellerId,memberId);
        int count = count(wrapper);
        /*// 查询用户发布的商品
        List<Goods> goodsList = list(wrapper);
        // 过滤掉未通过的商品
        List<Goods> goodsList1 = goodsList.stream().filter(goods -> {
            LambdaQueryWrapper<Audit> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(Audit::getGoodsId, goods.getId());
            Audit audit = auditService.getOne(queryWrapper);
            if (audit.getState() == 1) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());*/
        return count;
    }

    /**
     * 用户查询商品列表
     */
    @Override
    public List<GoodsDTO> goodsList(OrderGoodsPageReqDTO reqDTO) {
        LambdaQueryWrapper<Goods> wrapper = Wrappers.lambdaQuery();
        wrapper.like(reqDTO.getName()!=null,Goods::getName,reqDTO.getName())
                .like(reqDTO.getSeller()!=null,Goods::getSeller,reqDTO.getSeller());
        List<Goods> list = list(wrapper);
        List<GoodsDTO> goodsDTOS = list.stream().map(goods -> {
            GoodsDTO goodsDTO = new GoodsDTO();
            BeanUtils.copyProperties(goods, goodsDTO);
            goodsDTO.setCategoryName(categoryService.getById(goods.getCid()).getName());
            return goodsDTO;
        }).collect(Collectors.toList());
        return goodsDTOS;
    }

    /**
     * 查询商品详情
     */
    @Override
    public OrderGoodsDTO getorderGoodsInfo(Long id) {
        Goods goods = getById(id);
        OrderGoodsDTO orderGoodsDTO = new OrderGoodsDTO();
        BeanUtils.copyProperties(goods,orderGoodsDTO);
        return orderGoodsDTO;
    }

    /**
     * 重新发布
     * @param req
     */
    @Override
    public void updateRes(GoodsSaveTestReq req) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(req,goods);
        goods.setUpdateTime(LocalDateTime.now());
        this.baseMapper.updateById(goods);
        // 修改审核状态
        LambdaUpdateWrapper<Audit> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Audit::getGoodsId,req.getId())
                        .set(Audit::getState,0);
        auditService.update(wrapper);
    }

    /**
     * 查询用户发布商品id
     */
    @Override
    public List<Long> getGoodsById(Long memberId) {
        List<Long> ids = baseMapper.getGoodsId(memberId);
        return ids;
    }

    @Override
    public List<GoodsVO> listIds(List<Long> ids) {
        List<GoodsVO> goodsVO = baseMapper.listIds(ids);
        return goodsVO;
    }


}
