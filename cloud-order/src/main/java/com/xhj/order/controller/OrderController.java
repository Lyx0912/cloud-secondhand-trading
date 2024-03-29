package com.xhj.order.controller;

import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.order.entity.Order;
import com.xhj.order.entity.req.OrderListPageReq;
import com.xhj.order.entity.req.OrderPaymentReq;
import com.xhj.order.entity.req.OrderReq;
import com.xhj.order.entity.vo.*;
import com.xhj.order.service.OrderService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @Author: xhj
 * @Date: 2023/04/24/13:42
 * @Description:
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(OrderListPageReq req){
        PageUtils<OrderListVo> pageUtils = orderService.getAdminOrderPageList(req);
        System.out.println(req);
        return R.ok(pageUtils);
    }

    /**
     * 查询订单详情
     */
    @SneakyThrows
    @GetMapping("/info/{orderId}")
    public R getinfo(@PathVariable("orderId") Long orderId) {
        OrderInfoVo orderInfoVo = null;
        try {
            orderInfoVo = orderService.getInfo(orderId);
        } catch (Exception e) {
            return R.failed();
        }
        return R.ok(orderInfoVo);
    }
    /**
     * 查询订单、地址、发布数量
     */
    @GetMapping("/count/{memberId}")
    public R count(@PathVariable Long memberId){
        OrderReleaseAddrsCountVo countVo = orderService.getCount(memberId);
        return R.ok(countVo);
    }

    /**
     * 列表
     */
    @PostMapping("/list")
    public R qlist(@RequestBody OrderListPageReq req){
        PageUtils<OrderListVo> pageUtils = orderService.getAdminOrderPageList(req);
        return R.ok(pageUtils);
    }

    /**
     * 首页
     */
    @PostMapping("/dashboard")
    public R dashboard(){
//        PageUtils<OrderListVo> pageUtils = orderService.dashboard();
        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        Order order = orderService.getById(id);

        return R.ok(order);
    }

    /**
     * 订单查询
     */
    @GetMapping("/{OrderSn}/{memberId}")
    public R getOrderSn(@PathVariable("OrderSn") Long orderSn,
                        @PathVariable("memberId") Long memberId){
        OrderVo orderVo = orderService.getOrderSn(orderSn,memberId);
        return R.ok(orderVo);
    }

    /**
     * 订单准备
     */
    @PostMapping("/addOrder")
    public R addOrder(@RequestBody OrderReq req){
        OrderAddrVo orderAddrVo = orderService.getOrder(req);
        return R.ok(orderAddrVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody Order order){
        orderService.save(order);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody Order order){
        orderService.updateById(order);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 取消订单
     */
    @DeleteMapping("/deleteOrderByOrderId")
    public R deleteOrderByOrderId(OrderPaymentReq req){
        try {
            orderService.deleteOrderByOrderId(req);
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed();
        }
        return R.ok();
    }

    /**
     * 付款
     */
    @PostMapping("/payment")
    public R payment(@RequestBody OrderPaymentReq req){
        String status = null;
        try {
            status = orderService.payment(req);
        } catch (Exception e) {
            e.printStackTrace();
            return R.ok("0");
        }
        return R.ok(status);
    }
    /**
     * 安全信息获取
     */
    @GetMapping("/secure/{memberId}")
    public R secure(@PathVariable Long memberId){
        OrderSecureVo orderSecureVo = null;
        try {
            orderSecureVo = orderService.secure(memberId);
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed();
        }
        return R.ok(orderSecureVo);
    }
    /**
     * 确认收货
     */
    @GetMapping("/confirm/{orderSn}")
    public R confirm(@PathVariable String orderSn){
        try {
            orderService.confirm(orderSn);
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed();
        }
        return R.ok();
    }
}
