package com.xhj.order.entity.req;

import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/09/17/19:41
 * @Description:
 */
@Data
public class OrderPaymentReq {
    private Long orderSn;
    private Long payType;
    private Long memberId;
    private Long goodsId;
}
