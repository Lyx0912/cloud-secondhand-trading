package com.lyx.goods.controller;


import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Recommend;
import com.lyx.goods.entity.req.BannerListPageReq;
import com.lyx.goods.entity.req.RecommendListPageReq;
import com.lyx.goods.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-15 02:19:14
 */
@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

     /**
       * 分页查询推荐商品
       */
    @GetMapping("/list")
    public R list(RecommendListPageReq req){
        PageUtils<Recommend> pageUtils = recommendService.listPage(req);
        return R.ok();
    }
}

