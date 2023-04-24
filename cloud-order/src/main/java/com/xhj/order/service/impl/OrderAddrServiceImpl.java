package com.xhj.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhj.order.entity.OrderAddr;
import com.xhj.order.mapper.OrderAddrMapper;
import com.xhj.order.service.OrderAddrService;
import com.xhj.order.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * @Author: xhj
 * @Date: 2023/04/24/14:22
 * @Description:
 */
@Service
public class OrderAddrServiceImpl extends ServiceImpl<OrderAddrMapper, OrderAddr> implements OrderAddrService {
}
