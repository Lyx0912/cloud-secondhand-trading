package com.lyx.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Category;
import com.lyx.goods.entity.Goods;
import com.lyx.goods.entity.req.GoodsListPageReq;
import com.lyx.goods.entity.vo.GoodsVO;
import com.lyx.goods.mapper.GoodsMapper;
import com.lyx.goods.service.CategoryService;
import com.lyx.goods.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private CategoryService categoryService;

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
        // 查询VO所在分类
        List<Category> categories = categoryService.categoryTree();
        // todo 递归遍历分类，定位所在分类

        // 递归查找父分类
        if(null != goodsVO.getCategory()){
            goodsVO.setCategory(categoryService.findParentCategory(goodsVO.getCategory(),categories));
        }
        return goodsVO;
    }
}
