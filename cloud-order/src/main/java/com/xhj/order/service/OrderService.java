package com.xhj.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.order.entity.Order;
import com.xhj.order.entity.req.OrderListPageReq;
import com.xhj.order.entity.req.OrderReq;
import com.xhj.order.entity.vo.OrderListVo;
import com.xhj.order.entity.vo.OrderVo;
import org.springframework.stereotype.Service;

/**
 * @Author: xhj
 * @Date: 2023/04/24/13:50
 * @Description:
 */
public interface OrderService extends IService<Order> {

    PageUtils<OrderListVo> getOrderPageList(OrderListPageReq req);

    /**
     * 查询订单需要的信息
     * @param req
     * @return
     */
    OrderVo getOrder(OrderReq req);
}
