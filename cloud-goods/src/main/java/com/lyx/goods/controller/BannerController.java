package com.lyx.goods.controller;


import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Banner;
import com.lyx.goods.entity.req.BannerListPageReq;
import com.lyx.goods.entity.req.BannerSaveReq;
import com.lyx.goods.service.BannerService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-13 08:36:46
 */
@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

     /**
       * 分页查询轮播图列表
       */
    @GetMapping("/list")
    public R list(BannerListPageReq req) {
        PageUtils<Banner> pageUtils = bannerService.listPage(req);
        return R.ok(pageUtils);
    }

     /**
       * 获取轮播图详情
       */
    @GetMapping("/{id}")
    public R info(@PathVariable Long id){
        Banner banner = bannerService.getById(id);
        return R.ok(banner);
    }

     /**
       * 批量删除
       */
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable List<Long> ids){
        bannerService.removeByIds(ids);
        return R.ok();
    }

     /**
       * 添加轮播图
       */
    @PostMapping
    public R save(@Validated @RequestBody BannerSaveReq req){
        Banner banner = new Banner();
        BeanUtils.copyProperties(req,banner);
        bannerService.save(banner);
        return R.ok();
    }

    @PutMapping("/{id}")
    public R update(@PathVariable Long id,@Validated @RequestBody BannerSaveReq req){
        Banner banner = new Banner();
        BeanUtils.copyProperties(req,banner);
        banner.setId(id);
        bannerService.updateById(banner);
        return R.ok();
    }

    @GetMapping("/export")
    public void export(){

    }
}

