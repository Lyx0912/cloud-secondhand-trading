package com.xhj.order.controller;

import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.order.entity.Order;
import com.xhj.order.entity.req.OrderListPageReq;
import com.xhj.order.entity.req.OrderPaymentReq;
import com.xhj.order.entity.req.OrderReq;
import com.xhj.order.entity.vo.OrderAddrVo;
import com.xhj.order.entity.vo.OrderListVo;
import com.xhj.order.entity.vo.OrderVo;
import com.xhj.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

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
        PageUtils<OrderListVo> pageUtils = orderService.getOrderPageList(req);
        System.out.println(req);
        return R.ok(pageUtils.getList());
    }
    /**
     * 列表
     */
    @PostMapping("/list")
    public R qlist(@RequestBody OrderListPageReq req){
        PageUtils<OrderListVo> pageUtils = orderService.getOrderPageList(req);
        return R.ok(pageUtils);
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
        orderService.deleteOrderByOrderId(req);
        return R.ok();
    }

    /**
     * 付款
     */
    @PostMapping("/payment")
    public R payment(@RequestBody OrderPaymentReq req){
        String status = orderService.payment(req);
        return R.ok(status);
    }
}
