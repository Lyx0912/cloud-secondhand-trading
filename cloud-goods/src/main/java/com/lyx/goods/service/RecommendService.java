package com.lyx.goods.service;

import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Recommend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyx.goods.entity.req.RecommendListPageReq;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-15 02:19:14
 */
public interface RecommendService extends IService<Recommend> {

     /**
       * 分页查询首页推荐商品
       */
    PageUtils<Recommend> listPage(RecommendListPageReq req);
}
