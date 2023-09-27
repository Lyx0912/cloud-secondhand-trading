package com.xhj.order.service;

import com.lyx.common.mp.utils.PageUtils;
import com.xhj.order.entity.Pay;
import com.xhj.order.entity.req.OrderListPageReq;
import com.xhj.order.entity.vo.OrderListVo;
import com.xhj.order.entity.vo.PayVo;

/**
 * @Author: xhj
 * @Date: 2023/09/26/18:10
 * @Description:
 */
public interface PayService {
    /**
     * 保存支付信息
     * @param pay
     */
    void savePay(Pay pay);

    /**
     * 生成支付流水号
     * @return
     */
    String mzregNo();

    /**
     * 修改支付状态
     * @param i
     */
    void updateState(String orderSn, int i);

    /**
     * 分页查询
     * @param req
     * @return
     */
    PageUtils<PayVo> getPayPageList(OrderListPageReq req);
}
