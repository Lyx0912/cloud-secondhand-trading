package com.lyx.member.controller;

import com.lyx.common.base.result.R;
import com.lyx.common.base.result.ResultCode;
import com.lyx.member.entity.req.MemberLoginReq;
import com.lyx.member.entity.req.MemberPassReq;
import com.lyx.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/09/06/17:43
 * @Description:
 */
@RestController
@RequestMapping("/MemberLogin")
public class MemberLoginController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/login")
    public R login(@RequestBody MemberLoginReq req){
        List<MemberLoginReq> list = new ArrayList<>();
        list.add(req);
        return R.ok(list);
    }

    /**
     * 修改密码
     * @param req
     * @return
     */
    @PostMapping("/updatePassword")
    public R updatePassword(@RequestBody MemberPassReq req){
        try{
            memberService.updatePassword(req);
        }catch (Exception e){
            return R.failed(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
        return R.ok();
    }

    /**
     * 判断用户名是否存在
     * @param req
     * @return
     */
    @PostMapping("/getUserByUserName")
    public R getUserByUserName(@RequestBody MemberPassReq req){
        try{
            memberService.getUserByUserName(req);
        }catch (Exception e){
            return R.failed(ResultCode.USER_EXIST);
        }
        return R.ok();
    }

    /**
     * 用户注册
     * @param req
     * @return
     */
    @PostMapping("/register")
    public R register(@RequestBody MemberPassReq req){
        try{
            memberService.register(req);
        }catch (Exception e){
            return R.failed(ResultCode.USER_EXIST);
        }
        return R.ok();
    }



}
