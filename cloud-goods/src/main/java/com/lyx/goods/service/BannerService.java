package com.lyx.goods.service;

import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Banner;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyx.goods.entity.req.BannerListPageReq;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-13 08:36:46
 */
public interface BannerService extends IService<Banner> {

     /**
       * 分页查询banner
       */
    PageUtils<Banner> listPage(BannerListPageReq req);

    /**
     * 查询前十个轮播图
     * @return
     */
    List<Banner> bannerList();
}
