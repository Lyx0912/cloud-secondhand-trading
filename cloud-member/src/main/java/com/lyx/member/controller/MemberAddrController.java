package com.lyx.member.controller;


import com.lyx.common.base.entity.PageUtils;
import com.lyx.common.base.result.R;
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
       * 分页查询会员的收获地址
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
}

