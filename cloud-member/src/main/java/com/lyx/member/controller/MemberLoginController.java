package com.lyx.member.controller;

import com.lyx.common.base.result.R;
import com.lyx.member.entity.req.MemberLoginReq;
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

    @PostMapping("/login")
    public R login(@RequestBody MemberLoginReq req){
        List<MemberLoginReq> list = new ArrayList<>();
        list.add(req);
        return R.ok(list);
    }

}
