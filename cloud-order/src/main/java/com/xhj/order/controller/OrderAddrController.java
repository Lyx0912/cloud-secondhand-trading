package com.xhj.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.order.entity.Order;
import com.xhj.order.entity.OrderAddr;
import com.xhj.order.entity.req.OrderAddrListPageReq;
import com.xhj.order.entity.req.OrderListPageReq;
import com.xhj.order.entity.req.OrderReq;
import com.xhj.order.entity.vo.OrderAddrVo;
import com.xhj.order.entity.vo.OrderVo;
import com.xhj.order.service.OrderAddrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        orderAddrService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
