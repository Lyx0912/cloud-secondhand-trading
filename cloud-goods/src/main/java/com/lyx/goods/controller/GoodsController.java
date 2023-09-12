package com.lyx.goods.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lyx.common.base.entity.dto.GoodsDTO;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.common.base.result.R;
import com.lyx.common.web.utils.ResponseUtils;
import com.lyx.goods.entity.Goods;
import com.lyx.goods.entity.req.GoodsListPageReq;
import com.lyx.goods.entity.req.GoodsSaveReq;
import com.lyx.goods.entity.req.GoodsSaveTestReq;
import com.lyx.goods.entity.vo.GoodsVO;
import com.lyx.goods.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import utils.ExcelUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Wrapper;
import java.util.List;

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
@Slf4j
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

     /**
       * 分页查询商品列表
       */
    @GetMapping("/list")
    public R list(GoodsListPageReq req){
        PageUtils<GoodsVO> res = goodsService.listPage(req);
        return R.ok(res);
    }
     /**
       * 分页查询已上传商品列表
       */
    @PostMapping("/releaseGoodslist")
    public R releaseGoodslist(GoodsListPageReq req){
        PageUtils<GoodsVO> res = goodsService.releaseGoodslistPage(req);
        return R.ok(res);
    }

     /**
       * 删除商品
       */
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable List<Long> ids){
        // 逻辑删除商品
        goodsService.removeByIds(ids);
        return R.ok();
    }

     /**
       * 查询商品详情
       */
    @GetMapping("/{id}")
    public R info(@PathVariable Long id){
        GoodsVO byId = goodsService.getGoodsVOById(id);
        return R.ok(byId);
    }

     /**
       * 查询商品详情
       */
    @GetMapping("/infoFeign/{id}")
    public R infoFeign(@PathVariable Long id){
        GoodsVO byId = goodsService.getGoodsVOFeignById(id);
        return R.ok(byId);
    }

    /**
     * 查询订单商品详情
     */
    @GetMapping("/orderGoods/{id}")
    public GoodsDTO orderInfo(@PathVariable Long id){
        GoodsDTO byId = goodsService.getGoodsDTOById(id);
        return byId;
    }

     /**
       * 切换商品上架状态
       */
    @PatchMapping("/{isOnSell}")
    public R changeIsOnSell(@RequestBody List<Long> goodsIds ,@PathVariable("isOnSell")Integer isOnSell){
        goodsService.changeIsOnSell(goodsIds,isOnSell);
        log.info("goodsId{}",goodsIds);
        log.info("isOnSell{}",isOnSell);
        return R.ok();
    }

    /**
     * 更新商品信息
     */
    @PutMapping()
    public R update(@Validated @RequestBody GoodsSaveReq req) {
        goodsService.updateGoodsInfo(req);
        return R.ok();
    }

    /**
     * 发布商品信息
     */
    @PutMapping("/save")
    public R save(@Validated @RequestBody GoodsSaveTestReq req) {
        goodsService.saveGoodsInfo(req);
        return R.ok();
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<Goods> goodvos = goodsService.list();
        ExcelUtils.export(ResponseUtils.excelResponse(response).getOutputStream(),Goods.class,goodvos,"商品列表");
    }


}

