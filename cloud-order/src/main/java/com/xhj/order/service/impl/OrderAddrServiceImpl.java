package com.xhj.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhj.order.entity.OrderAddr;
import com.xhj.order.entity.req.OrderAddrListPageReq;
import com.xhj.order.entity.vo.OrderAddrVo;
import com.xhj.order.mapper.OrderAddrMapper;
import com.xhj.order.service.OrderAddrService;
import com.xhj.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/04/24/14:22
 * @Description:
 */
@Service
public class OrderAddrServiceImpl extends ServiceImpl<OrderAddrMapper, OrderAddr> implements OrderAddrService {

    @Autowired
    private  OrderAddrService orderAddrService;

    /**
     * 收货地址
     * @param req
     * @return
     */
    @Override
    public Page<OrderAddr> getOrderList(OrderAddrListPageReq req) {
        Page<OrderAddr> page = new Page<>(req.getPageNo(),req.getPageSize());
        LambdaQueryWrapper<OrderAddr> wrapper = Wrappers.lambdaQuery();
        Page<OrderAddr> addrPage = orderAddrService.page(page, wrapper);
        return addrPage;
    }
}
