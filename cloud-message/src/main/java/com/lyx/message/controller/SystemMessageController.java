package com.lyx.message.controller;


import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.message.entity.SystemMessage;
import com.lyx.message.entity.req.SystemMessageListPageReq;
import com.lyx.message.entity.vo.SystemMessageVO;
import com.lyx.message.service.SystemMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统消息表 前端控制器
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-17 03:06:01
 */
@RestController
@RequestMapping("/systemMessage")
public class SystemMessageController {

    @Autowired
    private SystemMessageService systemMessageService;

     /**
       * 分页查询系统消息
       */
    @GetMapping("/list")
    public R list(SystemMessageListPageReq req){
        PageUtils<SystemMessageVO> pageUtils = systemMessageService.listPage(req);
        return R.ok(pageUtils);
    }

}

