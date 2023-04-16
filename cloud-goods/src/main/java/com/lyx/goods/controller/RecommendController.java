package com.lyx.goods.controller;


import com.lyx.common.base.exception.BizException;
import com.lyx.common.base.result.R;
import com.lyx.common.base.result.ResultCode;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.common.web.utils.ResponseUtils;
import com.lyx.goods.entity.Goods;
import com.lyx.goods.entity.Recommend;
import com.lyx.goods.entity.req.RecommendListPageReq;
import com.lyx.goods.service.GoodsService;
import com.lyx.goods.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ExcelUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
    @Autowired
    private GoodsService goodsService;

     /**
       * 分页查询推荐商品
       */
    @GetMapping("/list")
    public R list(RecommendListPageReq req){
        PageUtils<Recommend> pageUtils = recommendService.listPage(req);
        return R.ok(pageUtils);
    }

     /**
       * 获取详情
       */
    @GetMapping("/{id}")
    public R info(@PathVariable Long id){
        Recommend recommend = recommendService.getById(id);
        return R.ok(recommend);
    }

     /**
       * 批量删除
       */
    @DeleteMapping("/{ids}")
    public R deletes(@PathVariable List<Long> ids){
        recommendService.removeByIds(ids);
        return R.ok();
    }

     /**
       * 同步商品信息
       */
    @PutMapping("/corret/{id}")
    public R update(@PathVariable Long id){
        recommendService.correct(id);
        return R.ok();
    }

     /**
       * 添加推荐商品
       */
    @PostMapping("/{goodsIds}")
    public R save(@PathVariable List<Long> goodsIds){
        recommendService.saveRecommend(goodsIds);
        return R.ok();
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<Recommend> recommends = recommendService.list();
        ExcelUtils.export(ResponseUtils.excelResponse(response).getOutputStream(),Recommend.class,recommends,"推荐商品列表");
    }

}

