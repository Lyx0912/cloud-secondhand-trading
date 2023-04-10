package com.lyx.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Category;
import com.lyx.goods.entity.Goods;
import com.lyx.goods.entity.GoodsDetails;
import com.lyx.goods.entity.GoodsImages;
import com.lyx.goods.entity.req.GoodsListPageReq;
import com.lyx.goods.entity.req.GoodsSaveReq;
import com.lyx.goods.entity.vo.GoodsVO;
import com.lyx.goods.mapper.GoodsMapper;
import com.lyx.goods.service.CategoryService;
import com.lyx.goods.service.GoodsDetailsService;
import com.lyx.goods.service.GoodsImagesService;
import com.lyx.goods.service.GoodsService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
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
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private GoodsImagesService imagesService;
    @Autowired
    private GoodsDetailsService detailsService;

    /**
     * 分页查询商品
     * @param req 查询条件对象
     */
    @Override
    public PageUtils<GoodsVO> listPage(GoodsListPageReq req) {
        // 构建分页对象 设置分页参数
        Page<GoodsVO> page = new Page<>(req.getPageNo(), req.getPageSize());
        // 构建查询条件
        LambdaQueryWrapper<Goods> wrapper = Wrappers.lambdaQuery();
        wrapper
                .like(StringUtils.isNotEmpty(req.getSeller()),Goods::getSeller,req.getSeller())
                .like(StringUtils.isNotEmpty(req.getName()),Goods::getName,req.getName())
                .eq(Goods::getDeleted,0)
                .eq(req.getIsOnSell()!=null,Goods::getIsOnSell,req.getIsOnSell());
        baseMapper.listPage(page,wrapper);
        PageUtils<GoodsVO> pageUtils = PageUtils.build(page);
        // 返回分页对象
        return pageUtils;
    }

    /**
     * 切换商品上架状态
     *
     * @param goodsId
     * @param isOnSell
     */
    @Override
    public void changeIsOnSell(Long goodsId, Integer isOnSell) {
        Goods goods = new Goods();
        goods.setId(goodsId);
        goods.setIsOnSell(isOnSell);
        baseMapper.updateById(goods);
        // 如果是下架就从es中删除商品信息并修改db中isOnsell
        if(isOnSell == 0){

            // todo 远程调用es
        }else {
            // 如果是上架就从es中添加商品信息
            // todo 远程调用es添加
        }
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
        List<GoodsImages> images = imagesService.lambdaQuery().eq(GoodsImages::getGoodsId, id).orderByAsc(GoodsImages::getIsDefault).select().list();
        goodsVO.setImages(images);
        // 设置商品详情
        GoodsDetails goodsDetails = detailsService.lambdaQuery().eq(GoodsDetails::getGoodsId, id).one();
        goodsVO.setDetails(goodsDetails);
        return goodsVO;
    }

    /**
     * 更新商品详情
     *
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGoodsIno(GoodsSaveReq req) {
        // 先拿到商品图片集合,拿到所有没id的对象进行入库操作。
        List<GoodsImages> images = req.getImages();
        List<GoodsImages> newImages = images.stream().filter(item -> item.getId() == null).collect(Collectors.toList());
        // 批量插入
        imagesService.saveBatch(newImages);
        // 收集id
        images.addAll(newImages);
        List<Long> allId = images.stream().filter(item -> item.getId() != null).map(item -> item.getId()).collect(Collectors.toList());
        // 从db中删除不存在的图片(前端执行了移除操作后列表中就不存在了)
        imagesService.lambdaUpdate().notIn(!CollectionUtils.isEmpty(allId),GoodsImages::getId,allId);


        // 更新商品详情
        detailsService.updateById(req.getDetails());
        // 更新商品信息
        Goods goods = new Goods();
        BeanUtils.copyProperties(req,goods);
        updateById(goods);
        // todo 更新elasticsearch
    }
}
