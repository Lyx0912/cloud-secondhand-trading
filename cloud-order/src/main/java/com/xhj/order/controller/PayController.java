package com.xhj.order.controller;

import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.order.entity.req.OrderListPageReq;
import com.xhj.order.entity.vo.OrderListVo;
import com.xhj.order.entity.vo.PayVo;
import com.xhj.order.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: xhj
 * @Date: 2023/09/26/14:27
 * @Description: 支付
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayService payService;

    @RequestMapping("/test")
    public String test(){
        return payService.mzregNo();
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public R getPayPageList(OrderListPageReq req){
        PageUtils<PayVo> pageUtils = payService.getPayPageList(req);
        return R.ok(pageUtils);
    }

}
