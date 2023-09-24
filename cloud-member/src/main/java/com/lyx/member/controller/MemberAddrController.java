package com.lyx.member.controller;


import com.lyx.common.mp.utils.PageUtils;
import com.lyx.common.base.result.R;
import com.lyx.common.web.utils.ResponseUtils;
import com.lyx.member.entity.MemberAddr;
import com.lyx.member.entity.req.MemberAddrPageReq;
import com.lyx.member.entity.req.MemberAddrReq;
import com.lyx.member.entity.req.SaveMemberAddrReq;
import com.lyx.member.entity.vo.MemberAddrOneVo;
import com.lyx.member.entity.vo.MemberAddrVO;
import com.lyx.member.service.MemberAddrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
       * 根据id查询收货地址
       */
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id){
        MemberAddrOneVo memberAddrVO = memberAddrService.getMemberAddr(id);
        return R.ok(memberAddrVO);
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
     * 用户添加收货地址
     */
    @PostMapping("/savememberAddr")
    public R saveMemberAddrs(@Validated @RequestBody MemberAddrReq req){
        memberAddrService.saveMemberAddrs(req);
        return R.ok();
    }

    /**
     * 用户修改收货地址
     */
    @PostMapping("/updateMemberAddr")
    public R updateMemberAddr(@Validated @RequestBody MemberAddrReq req){
        MemberAddr memberAddr = memberAddrService.updateMemberAddr(req);
        return R.ok(memberAddr);
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

       */
    @GetMapping ("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<MemberAddr> addrs = memberAddrService.list();
        ExcelUtils.export(ResponseUtils.excelResponse(response).getOutputStream(),MemberAddr.class,addrs,"会员地址列表");
    }
}

