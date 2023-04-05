package com.lyx.goods.controller;


import com.lyx.common.base.entity.PageUtils;
import com.lyx.common.base.result.R;
import com.lyx.goods.entity.req.GoodsListPageReq;
import com.lyx.goods.entity.vo.GoodsVO;
import com.lyx.goods.service.GoodsService;
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
 * @since 2023-03-31 11:16:28
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/list")
    public R list(GoodsListPageReq req){
        PageUtils<GoodsVO> res = goodsService.listPage(req);
        return R.ok(res);
    }

}

