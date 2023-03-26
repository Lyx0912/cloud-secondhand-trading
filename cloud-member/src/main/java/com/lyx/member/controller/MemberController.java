package com.lyx.member.controller;


import com.lyx.common.base.entity.PageUtils;
import com.lyx.common.base.result.R;
import com.lyx.member.entity.req.MemberListPageReq;
import com.lyx.member.entity.vo.MemberVO;
import com.lyx.member.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-25 09:39:17
 */
@Controller
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;

     /**
       * 分页查询会员列表
       */
    @GetMapping("/list")
    public R list(MemberListPageReq req){
        PageUtils<MemberVO> page = memberService.pageMember(req);
        return R.ok(page);
    }

}

