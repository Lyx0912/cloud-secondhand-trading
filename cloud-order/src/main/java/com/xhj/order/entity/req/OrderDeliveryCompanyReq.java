package com.xhj.order.entity.req;

import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/09/25/16:31
 * @Description:
 */
@Data
public class OrderDeliveryCompanyReq {
    private Long orderId;
    private String deliverySn;
    private String deliveryCompany;
}
