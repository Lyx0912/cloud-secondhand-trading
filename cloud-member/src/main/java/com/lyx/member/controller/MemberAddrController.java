package com.lyx.member.controller;


import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.lyx.common.base.entity.PageUtils;
import com.lyx.common.base.result.R;
import com.lyx.member.entity.Member;
import com.lyx.member.entity.MemberAddr;
import com.lyx.member.entity.req.MemberAddrPageReq;
import com.lyx.member.entity.req.MemberListPageReq;
import com.lyx.member.entity.req.SaveMemberAddrReq;
import com.lyx.member.entity.vo.MemberAddrVO;
import com.lyx.member.entity.vo.MemberVO;
import com.lyx.member.service.MemberAddrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import utils.ExcelUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 用户地址 前端控制器
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-25 09:42:34
 */
@RestController
@RequestMapping("/memberAddr")
public class MemberAddrController {

    @Autowired
    public MemberAddrService memberAddrService;

     /**
       * 分页查询指定会员的收获地址
       */
    @GetMapping("/list")
    public R list(MemberAddrPageReq req){
        PageUtils<MemberAddrVO> page = memberAddrService.pageMemberAddr(req);
        return R.ok(page);
    }

    /**
     * 添加收货地址
     */
    @PostMapping
    public R save(@Validated @RequestBody SaveMemberAddrReq req){
        memberAddrService.saveMemberAddr(req);
        return R.ok();
    }

    /**
     * 根据id删除
     */
    @DeleteMapping("/{ids}")
    public R deleteBatch(@PathVariable List<Long> ids){
        memberAddrService.removeByIds(ids);
        return R.ok();
    }

     /**
       * 导出excel
       * todo 测试接口
       */
    @GetMapping ("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<MemberAddr> addrs = memberAddrService.list();
        response.setContentType("application/vnd.ms-excel");// 设置文本内省
        response.setCharacterEncoding("utf-8");// 设置字符编码
        response.setHeader("Content-disposition", "attachment;filename=demo.xlsx"); // 设置响应头
        ExcelUtils.export(response.getOutputStream(),MemberAddr.class,addrs,"会员收货地址");
    }
}

