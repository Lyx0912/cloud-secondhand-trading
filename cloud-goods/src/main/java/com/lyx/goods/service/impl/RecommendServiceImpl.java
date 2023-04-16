package com.lyx.goods.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyx.common.base.exception.BizException;
import com.lyx.common.base.result.ResultCode;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Goods;
import com.lyx.goods.entity.Recommend;
import com.lyx.goods.entity.req.RecommendListPageReq;
import com.lyx.goods.mapper.RecommendMapper;
import com.lyx.goods.service.GoodsService;
import com.lyx.goods.service.RecommendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-15 02:19:14
 */
@Service
public class RecommendServiceImpl extends ServiceImpl<RecommendMapper, Recommend> implements RecommendService {

    @Autowired
    private GoodsService goodsService;

    /**
     * 分页查询首页推荐商品
     * @param req
     */
    @Override
    public PageUtils<Recommend> listPage(RecommendListPageReq req) {
        // 构建分页对象 设置分页参数
        Page<Recommend> page = new Page<>(req.getPageNo(),req.getPageSize());
        // 构建查询条件
        LambdaQueryWrapper<Recommend> wrapper = Wrappers.lambdaQuery();

        baseMapper.selectPage(page,wrapper);
        PageUtils<Recommend> pageUtils = new PageUtils<>();
        // 转换vo
        pageUtils.setList(page.getRecords());
        pageUtils.setPageNo(page.getCurrent());
        pageUtils.setTotal(page.getTotal());
        // 返回分页对象
        return pageUtils;
    }

    /**
     * 添加商品推荐
     *
     * @param goodsIds
     */
    @Override
    public void saveRecommend(List<Long> goodsIds) {
        // 检查推荐的商品数量是否已经到上限
        int count = this.count();
        if(count > 16){
            throw new BizException(ResultCode.EXCEED_THE_RECOMMEND);
        }
        // 是否已存在被推荐商品
        Integer isExist = lambdaQuery().in(Recommend::getGoodsId, goodsIds).count();
        if(isExist > 0){
            throw new BizException(ResultCode.GOODS_ALREADY_EXIST);
        }
        // 查询商品详细信息后批量插入
        List<Goods> goods = goodsService.lambdaQuery().in(Goods::getId, goodsIds).list();
        List<Recommend> recommends = new ArrayList<>();
        for (Goods good : goods) {
            Recommend recommend = new Recommend();
            recommend.setGoodsId(good.getId());
            recommend.setGoodsImage(good.getDefaultImg());
            recommend.setGoodsName(good.getName());
            recommend.setGoodsDescription(good.getDescription());
            recommend.setGoodsPrice(good.getPrice());
            recommends.add(recommend);
        }
        saveBatch(recommends);
    }

    /**
     * 根据商品id同步数据
     *
     * @param id
     */
    @Override
    public void correct(Long id) {
        Recommend recommend = getById(id);
        Goods goods = goodsService.getById(recommend.getGoodsId());
        if(goods == null){
            throw new BizException(ResultCode.DATA_NOT_EXIST);
        }
        recommend.setGoodsImage(goods.getDefaultImg());
        recommend.setGoodsName(goods.getName());
        recommend.setGoodsDescription(goods.getDescription());
        recommend.setGoodsPrice(goods.getPrice());
        updateById(recommend);
    }
}
