package com.xhj.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.order.entity.Order;
import com.xhj.order.entity.req.OrderListPageReq;
import org.springframework.stereotype.Service;

/**
 * @Author: xhj
 * @Date: 2023/04/24/13:50
 * @Description:
 */
public interface OrderService extends IService<Order> {

    PageUtils<Order> getOrderPageList(OrderListPageReq req);

}
