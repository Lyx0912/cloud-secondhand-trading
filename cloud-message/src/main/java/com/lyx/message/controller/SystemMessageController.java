package com.lyx.message.controller;


import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.common.web.utils.ResponseUtils;
import com.lyx.common.web.utils.UserContext;
import com.lyx.message.entity.SystemMessage;
import com.lyx.message.entity.req.SystemMessageListPageReq;
import com.lyx.message.entity.req.SystemMessageSaveReq;
import com.lyx.message.entity.vo.SystemMessageVO;
import com.lyx.message.service.SystemMessageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import utils.ExcelUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

     /**
       * 获取详情
       */
    @GetMapping("/{id}")
    public R info(@PathVariable Long id){
        SystemMessage systemMessage = systemMessageService.getById(id);
        return R.ok(systemMessage);
    }

     /**
       * 新增系统公告
       */
    @PostMapping
    public R save(@Validated @RequestBody SystemMessageSaveReq req){
        req.setCreater(UserContext.getCurrentUserName());
        SystemMessage systemMessage = new SystemMessage();
        BeanUtils.copyProperties(req,systemMessage);
        systemMessageService.save(systemMessage);
        return R.ok();
    }

     /**
       * 更新公告
       */
    @PutMapping
    public R update(@Validated @RequestBody SystemMessageSaveReq req){
        SystemMessage systemMessage = new SystemMessage();
        BeanUtils.copyProperties(req,systemMessage);
        systemMessageService.updateById(systemMessage);
        return R.ok();
    }

     /**
       * 批量删除
       */
    @DeleteMapping("/{ids}")
    public R deletes(@PathVariable List<Long> ids){
        systemMessageService.removeByIds(ids);
        return R.ok();
    }

     /**
       * 导出
       */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<SystemMessage> systemMessages = systemMessageService.list();
        ExcelUtils.export(ResponseUtils.excelResponse(response).getOutputStream(),SystemMessage.class,systemMessages,"系统公告列表");
    }
}

