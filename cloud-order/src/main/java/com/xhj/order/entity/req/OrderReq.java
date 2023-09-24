package com.xhj.order.entity.req;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: xhj
 * @Date: 2023/09/07/14:38
 * @Description:
 */
@Data
public class OrderReq {
    private Long goodsId;
    private Long memberId;
    private Long addrId;
    private BigDecimal price;
    private String memberName;
}
