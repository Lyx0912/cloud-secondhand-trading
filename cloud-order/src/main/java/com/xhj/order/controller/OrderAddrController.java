package com.xhj.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.order.entity.Order;
import com.xhj.order.entity.OrderAddr;
import com.xhj.order.entity.req.*;
import com.xhj.order.entity.vo.OrderAddrMemberIdVo;
import com.xhj.order.entity.vo.OrderAddrVo;
import com.xhj.order.entity.vo.OrderInfoVo;
import com.xhj.order.entity.vo.OrderVo;
import com.xhj.order.service.OrderAddrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @Author: xhj
 * @Date: 2023/04/24/14:27
 * @Description:
 */
@RestController
@RequestMapping("/orderaddr")
public class OrderAddrController {

    @Autowired
    private OrderAddrService orderAddrService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(OrderAddrListPageReq req){
        Page<OrderAddr> orderAddrVos = orderAddrService.getOrderList(req);
        System.out.println(req);
        return R.ok(orderAddrVos);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        OrderAddr orderAddr = orderAddrService.getById(id);

        return R.ok(orderAddr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OrderAddr orderAddr){
        orderAddrService.save(orderAddr);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OrderAddr orderAddr){
        orderAddrService.updateById(orderAddr);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/updates")
    public R updateAddr(@RequestBody OrderAddrReq req){
        orderAddrService.updateAddr(req);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        orderAddrService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 填写快递单号
     */
    @PostMapping("/updateOrder")
    public R updateOrder(@RequestBody OrderDeliveryCompanyReq req){
        orderAddrService.updateOrder(req);
        return R.ok();
    }


    /**
     * 查询商品所有消息
     */
    @GetMapping("/getGoods")
    public R getGoods(Long goodsId)  {
        OrderReq req = new OrderReq();
        req.setGoodsId(goodsId);
        OrderAddrMemberIdVo orderAddrMemberIdVo = null;
        try {
            orderAddrMemberIdVo = orderAddrService.getGoods(req);
        } catch (Exception e) {
            e.printStackTrace();
            R.failed();
        }
        return R.ok(orderAddrMemberIdVo);
    }



}
