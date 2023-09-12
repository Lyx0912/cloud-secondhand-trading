package com.lyx.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Banner;
import com.lyx.goods.entity.req.BannerListPageReq;
import com.lyx.goods.mapper.BannerMapper;
import com.lyx.goods.service.BannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-13 08:36:46
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    /**
     * 分页查询banner
     *
     * @param req
     */
    @Override
    public PageUtils<Banner> listPage(BannerListPageReq req) {
        Page<Banner> page = new Page<>(req.getPageNo(),req.getPageSize());
        LambdaQueryWrapper<Banner> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotEmpty(req.getTitle()), Banner::getTitle, req.getTitle())
                .like(!StringUtils.isEmpty(req.getDescription()),Banner::getDescription,req.getDescription())
                .eq(req.getIsActivite() != null, Banner::getIsActive, req.getIsActivite());
        this.baseMapper.selectPage(page, wrapper);
        return PageUtils.build(page);
    }

    /**
     * 查询前十个轮播图
     * @return
     */
    @Override
    public List<Banner> bannerList() {
        Page<Banner> page = new Page<>(1,10);
        QueryWrapper<Banner> wrapper = new QueryWrapper<>();
        wrapper.eq("is_active", 1);
        Page<Banner> bannerPage = this.baseMapper.selectPage(page, wrapper);
        return bannerPage.getRecords();
    }
}
