package com.lyx.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Goods;
import com.lyx.goods.entity.req.GoodsListPageReq;
import com.lyx.goods.entity.vo.GoodsVO;
import com.lyx.goods.mapper.GoodsMapper;
import com.lyx.goods.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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

    /**
     * 分页查询商品
     * todo 未完成代码逻辑
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
}
