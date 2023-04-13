package com.lyx.goods.controller;


import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Banner;
import com.lyx.goods.entity.req.BannerListPageReq;
import com.lyx.goods.entity.req.BannerSaveReq;
import com.lyx.goods.service.BannerService;
import org.apache.commons.lang3.RandomStringUtils;
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
        // 随机生成1000个会员并插入数据库
        List<Banner> list = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            Banner banner = new Banner();
            banner.setTargetUrl(RandomStringUtils.randomAlphabetic(10));
            banner.setDescription(RandomStringUtils.randomAlphabetic(10));
            banner.setImageUrl("https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/6b996f91f354815eb138d63f2e7e9391.jpg?w=2452&h=920");
            banner.setTargetUrl("https://www.mi.com/shop");
            banner.setSort(0);
            banner.setIsActive(0);
            list.add(banner);
        }
        bannerService.saveBatch(list);
        return R.ok();
    }

    @GetMapping("/export")
    public void export(){

    }
}

