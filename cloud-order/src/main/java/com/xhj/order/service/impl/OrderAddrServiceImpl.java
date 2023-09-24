package com.xhj.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.common.base.result.R;
import com.xhj.order.entity.Order;
import com.xhj.order.entity.OrderAddr;
import com.xhj.order.entity.req.OrderAddrListPageReq;
import com.xhj.order.entity.req.OrderAddrReq;
import com.xhj.order.entity.req.OrderReq;
import com.xhj.order.entity.vo.OrderInfoVo;
import com.xhj.order.feign.MemberFeignService;
import com.xhj.order.mapper.OrderAddrMapper;
import com.xhj.order.service.OrderAddrService;
import com.xhj.order.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: xhj
 * @Date: 2023/04/24/14:22
 * @Description:
 */
@Service
public class OrderAddrServiceImpl extends ServiceImpl<OrderAddrMapper, OrderAddr> implements OrderAddrService {

    @Autowired
    private  OrderAddrService orderAddrService;

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private MemberFeignService memberFeignService;

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
    /**
     * 修改
     */
    @Override
    public void updateAddr(OrderAddrReq req) {
        // 修改订单地址
        OrderAddr orderAddr = req.getOrderAddr();
        orderAddr.setId(req.getId());
        this.baseMapper.updateById(orderAddr);

    }

    /**
     * 查询商品所有消息
     */
    @Override
    public OrderInfoVo getGoods(OrderReq req) {
        // 查询商品信息
        R byId = memberFeignService.getById(req.getGoodsId());

        // 查询订单地址 和 用户

        return null;
    }
}
