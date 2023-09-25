package com.lyx.member.controller;


import com.lyx.common.base.result.IResultCode;
import com.lyx.common.base.result.ResultCode;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.common.base.result.R;
import com.lyx.common.web.utils.ResponseUtils;
import com.lyx.member.entity.Member;
import com.lyx.member.entity.req.MemberLoginReq;
import com.lyx.member.entity.req.MemberListPageReq;
import com.lyx.member.entity.req.MemberPassReq;
import com.lyx.member.entity.req.SaveMemberReq;
import com.lyx.member.entity.vo.AreaVo;
import com.lyx.member.entity.vo.MemberLoginVo;
import com.lyx.member.entity.vo.MemberVO;
import com.lyx.member.service.AreaService;
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
     * 查询用户是否存在
     * @param name
     * @return
     */
    @PostMapping("/getName/{name}")
    public R getName(@PathVariable("name")String name){
        String memberName = memberService.getName(name);
        if (memberName==null){
            return R.failed(ResultCode.USER_NOT_EXIST);
        }
        return R.ok();
    }

    /**
     * 批量查询
     */
    @PostMapping("/getList")
    public R getList(@RequestBody List<Long> ids){
        List<Member> members = memberService.getList(ids);
        return R.ok(members);
    }
    /**
     * 会员登录
     */
    @PostMapping("/login")
    public R login(@RequestBody MemberLoginReq req){
        MemberLoginVo memberLoginVo = memberService.login(req);
        if (memberLoginVo==null){
            return R.failed(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
        return R.ok(memberLoginVo);
    }
    /**
     * 会员修改
     */
    @PostMapping("/updata")
    public R update(@RequestBody MemberListPageReq req){
        try{
            memberService.updateMember(req);
        }catch (Exception e){
            return R.failed(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
        return R.ok();
    }

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
       * 获取手机号
       */
    @GetMapping("/memberMobile/{id}")
    public String getMemberMobile(@NotNull(message = "id can not be null!") @PathVariable Long id){
        String mobile = memberService.getMemberMobile(id);
        return mobile;
    }
     /**
       * 获取会员详情
       */
    @GetMapping("/memberId/{id}")
    public R getMemberById(@NotNull(message = "id can not be null!") @PathVariable Long id){
        MemberVO vo = memberService.getMemberById(id);
        return R.ok(vo);
    }

     /**
       * 密码对比
       */
    @PostMapping("/getPassword")
    public R getPassword( @RequestBody MemberPassReq req){
        try{
            memberService.getPassword(req);
        }catch (Exception e){
            return R.failed(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }

        return R.ok();
    }

    /**
     * 查询用户发布商品数量
     */
    @GetMapping("/count/{memberId}")
    public R count(@PathVariable Long memberId){
        Integer count = memberService.countMemberId(memberId);
        return R.ok(count);
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
