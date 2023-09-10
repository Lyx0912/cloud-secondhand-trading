package com.xhj.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xhj.order.entity.OrderAddr;
import com.xhj.order.entity.req.OrderAddrListPageReq;
import com.xhj.order.entity.vo.OrderAddrVo;

import java.util.List;
import java.util.Map;

/**
 * 用户订单配送地址
 *
 * @Author xhj
 * @date 2023-04-24 14:00:41
 * @Description:
 */
public interface OrderAddrService extends IService<OrderAddr> {

    /**
     * 地址分页
     * @param req
     * @return
     */
    Page<OrderAddr> getOrderList(OrderAddrListPageReq req);
}

