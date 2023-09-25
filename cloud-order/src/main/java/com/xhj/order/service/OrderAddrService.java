package com.xhj.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xhj.order.entity.OrderAddr;
import com.xhj.order.entity.req.OrderAddrListPageReq;
import com.xhj.order.entity.req.OrderAddrReq;
import com.xhj.order.entity.req.OrderDeliveryCompanyReq;
import com.xhj.order.entity.req.OrderReq;
import com.xhj.order.entity.vo.OrderAddrMemberIdVo;
import com.xhj.order.entity.vo.OrderAddrVo;
import com.xhj.order.entity.vo.OrderInfoVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
    /**
     * 修改
     */
    void updateAddr(OrderAddrReq req);
    /**
     * 查询商品所有消息
     */
    OrderAddrMemberIdVo getGoods(OrderReq req) throws ExecutionException, InterruptedException;
    /**
     * 填写快递单号
     */
    void updateOrder(OrderDeliveryCompanyReq req);
}

