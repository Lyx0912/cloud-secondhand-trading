package com.lyx.goods.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Recommend;
import com.lyx.goods.entity.req.RecommendListPageReq;
import com.lyx.goods.mapper.RecommendMapper;
import com.lyx.goods.service.RecommendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

    /**
     * 分页查询首页推荐商品
     *
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
}
