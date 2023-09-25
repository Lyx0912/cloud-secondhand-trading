package com.xhj.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.common.base.entity.dto.GoodsEsDTO;
import com.lyx.common.base.entity.dto.GoodsVoDTO;
import com.lyx.common.base.entity.dto.OrderGoodsDTO;
import com.lyx.common.base.result.R;
import com.xhj.order.entity.Order;
import com.xhj.order.entity.OrderAddr;
import com.xhj.order.entity.req.*;
import com.xhj.order.entity.vo.OrderAddrMemberIdVo;
import com.xhj.order.entity.vo.OrderInfoVo;
import com.xhj.order.feign.GoodsFeignService;
import com.xhj.order.feign.MemberFeignService;
import com.xhj.order.mapper.OrderAddrMapper;
import com.xhj.order.service.OrderAddrService;
import com.xhj.order.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

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
    private  OrderService orderService;

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private MemberFeignService memberFeignService;
    @Autowired
    private GoodsFeignService goodsFeignService;

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
    public OrderAddrMemberIdVo getGoods(OrderReq req) throws ExecutionException, InterruptedException {
        OrderAddrMemberIdVo memberIdVo = new OrderAddrMemberIdVo();
        // 查询商品信息
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            R r = goodsFeignService.orderFeignInfo(req.getGoodsId());
            String s = JSONObject.toJSONString(r.getData());
            GoodsVoDTO goodsVoDTO = JSON.parseObject(s, GoodsVoDTO.class);
            memberIdVo.setGoodsDTO(goodsVoDTO);
        }, executor);

        // 查询订单地址
        CompletableFuture<List<Order>> supplyAsync = CompletableFuture.supplyAsync(() -> {
            LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Order::getGoodsId, req.getGoodsId());
            List<Order> orders = orderService.list(wrapper);
            return orders;
        }, executor);
        List<OrderInfoVo> orderInfoVos = new ArrayList<>();
        // 用户
        CompletableFuture<Void> thenAcceptAsync = supplyAsync.thenAcceptAsync(orders -> {
            for (Order order : orders) {
                OrderInfoVo orderInfoVo = new OrderInfoVo();
                LambdaQueryWrapper<OrderAddr> queryWrapper = Wrappers.lambdaQuery();
                queryWrapper.eq(OrderAddr::getId, order.getOrderAddrId());
                OrderAddr orderAddrs = getOne(queryWrapper);
                orderInfoVo.setOrderAddr(orderAddrs);
                orderInfoVo.setOrder(order);
                orderInfoVos.add(orderInfoVo);
            }
            memberIdVo.setOrderInfoVos(orderInfoVos);
        }, executor);
        CompletableFuture.allOf(voidCompletableFuture,supplyAsync,thenAcceptAsync).get();
        return memberIdVo;
    }

    /**
     * 填写快递单号
     */
    @Override
    public void updateOrder(OrderDeliveryCompanyReq req) {
        // state=2  delivery_company 快递 delivery_sn单号  delivery_time发货时间
        LambdaUpdateWrapper<Order> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Order::getId,req.getOrderId())
                .set(Order::getState,2)
                .set(Order::getDeliveryCompany,req.getDeliveryCompany())
                .set(Order::getDeliverySn,req.getDeliverySn())
                .set(Order::getDeliveryTime, LocalDateTime.now());
        orderService.update(wrapper);
    }
}
