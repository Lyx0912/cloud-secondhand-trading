package com.xhj.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.common.base.entity.dto.GoodsDTO;
import com.lyx.common.base.entity.dto.GoodsEsDTO;
import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.order.entity.Order;
import com.xhj.order.entity.OrderAddr;
import com.xhj.order.entity.req.OrderListPageReq;
import com.xhj.order.entity.req.OrderReq;
import com.xhj.order.entity.vo.OrderListVo;
import com.xhj.order.entity.vo.OrderVo;
import com.xhj.order.feign.GoodsFeignService;
import com.xhj.order.mapper.OrderMapper;
import com.xhj.order.service.OrderAddrService;
import com.xhj.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: xhj
 * @Date: 2023/04/24/13:51
 * @Description:
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Order> implements OrderService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderAddrService orderAddrService;

    @Autowired
    private GoodsFeignService goodsFeignService;

    @Override
    public PageUtils<OrderListVo> getOrderPageList(OrderListPageReq req) {
        Page<OrderListVo> page = new Page<>(req.getPageNo(), req.getPageSize());
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        // TODO 完善查询条件
        List<Order> orders = this.baseMapper.getOrderList();
        List<OrderListVo> orderListVos = orders.stream().map(order -> {
            OrderListVo orderListVo = new OrderListVo();
            BeanUtils.copyProperties(order, orderListVo);
            orderListVo.setBuyer(order.getMemberUsername());
            // 远程调用查询卖家 goods_id
            GoodsDTO goodsDTO = goodsFeignService.orderInfo(order.getGoodsId());
            if (goodsDTO!=null){
                orderListVo.setSeller(goodsDTO.getSeller());
                orderListVo.setState(order.getState());
                orderListVo.setDefaultImg(goodsDTO.getDefaultImg());
                orderListVo.setCategoryName(goodsDTO.getCategoryName());
                orderListVo.setGoodsName(goodsDTO.getName());
            }
            return orderListVo;
        }).collect(Collectors.toList());
        page.setRecords(orderListVos);
        return PageUtils.build(page);
    }

    @Override
    public OrderVo getOrder(OrderReq req) {
        // 查询用户收货地址
        List<OrderAddr> list = orderAddrService.list();
        // 查询商品信息
        GoodsDTO goodsDTO = goodsFeignService.orderInfo(req.getGoodsId());
        OrderVo orderVo = new OrderVo();
        log.info("info{}",goodsDTO);
        orderVo.setGoodsDTO(goodsDTO);
        orderVo.setOrderAddr(list);
        return orderVo;
    }
}
