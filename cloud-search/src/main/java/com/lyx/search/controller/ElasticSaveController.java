package com.lyx.search.controller;

import com.lyx.common.base.result.R;
import com.lyx.search.entity.vo.GoodsEsModel;
import com.lyx.search.service.GoodsSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/04/19/14:45
 * @Description:
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class ElasticSaveController {

    @Autowired
    private GoodsSaveService goodsSaveService;

    @PostMapping("/goods")
    public R goodsStatusUp(@RequestBody List<GoodsEsModel> goodsEsModels){
        System.out.println(goodsEsModels);
        log.info("goodsEsModels{}","goodsEsModels");
//        try {
//            goodsSaveService.goodsStatusUp(goodsEsModels);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return R.ok("goodsEsModels");
    }

}
