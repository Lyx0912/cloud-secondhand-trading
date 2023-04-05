package com.lyx.member.controller;


import com.lyx.common.base.entity.PageUtils;
import com.lyx.common.base.result.R;
import com.lyx.common.web.utils.ResponseUtils;
import com.lyx.member.entity.Member;
import com.lyx.member.entity.req.MemberListPageReq;
import com.lyx.member.entity.req.SaveMemberReq;
import com.lyx.member.entity.vo.MemberVO;
import com.lyx.member.service.MemberAddrService;
import com.lyx.member.service.MemberService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import utils.ExcelUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-25 09:39:17
 */
@RestController
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

     /**
       * 获取会员详情
       */
    @GetMapping("/{id}")
    public R getById(@NotNull(message = "id can not be null!") @PathVariable Long id){
        MemberVO vo = memberService.getMemberVO(id);
        return R.ok(vo);
    }

     /**
       * 更新会员账号启用状态
       */
    @PatchMapping("/{id}")
    public R updateStatus(@NotBlank(message = "会员编号不能为空") @PathVariable Long id,@NotBlank(message = "状态不能为空") @RequestParam Integer status){
        Member member = new Member();
        member.setId(id);
        member.setStatus(status);
        return R.ok(memberService.updateById(member));
    }

     /**
       * 批量删除会员
       */
    @DeleteMapping("/{ids}")
    public R deleteMember(@Size(min = 1,message = "会员编号不能为空") @PathVariable List<Long> ids){
        memberService.removeByIds(ids);
        return R.ok();
    }

    @PutMapping("/{id}")
    public R update(@Validated @RequestBody SaveMemberReq req, @PathVariable Long id){
        // 更新member信息
        Member member = new Member();
        member.setId(id);
        BeanUtils.copyProperties(req,member);
        // 更新用户信息
        memberService.updateById(member);
        return R.ok();
    }

     /**
       * 导出所有会员信息
       */
    @GetMapping ("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<Member> members = memberService.list();
        ExcelUtils.export(ResponseUtils.excelResponse(response).getOutputStream(),Member.class,members,"会员列表");
    }

     /**
       * 随机生成1000个会员入库
       */
    @PostMapping
    public R save(){
        // 随机生成1000个会员并插入数据库
        List<Member> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Member member = new Member();
            member.setUsername(RandomStringUtils.randomAlphabetic(10));
            member.setNickname(RandomStringUtils.randomAlphabetic(10));
            member.setBirth(LocalDate.now());
            member.setPassword("123456");
            member.setMobile("15673225896");
            member.setEmail("1677685900@qq.com");
            member.setGender(1);
            list.add(member);
        }
        memberService.saveBatch(list);
        return R.ok();
    }

}

