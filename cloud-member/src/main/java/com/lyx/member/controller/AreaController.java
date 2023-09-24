package com.lyx.member.controller;


import com.lyx.common.base.result.R;
import com.lyx.member.entity.Area;
import com.lyx.member.entity.vo.AreaVo;
import com.lyx.member.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 省市区地区信息 前端控制器
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-25 09:42:34
 */
@RestController
@RequestMapping("/area")
public class AreaController {

    @Autowired
    private AreaService areaService;

    /**
     * 省市区地区信息
     * @return
     */
    @PostMapping("/list")
    public R list(){
        List<AreaVo> areaList = areaService.areaList();
        return R.ok(areaList);
    }

}

