package com.xhj.order.entity.req;

import com.xhj.order.entity.OrderAddr;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: xhj
 * @Date: 2023/09/24/19:15
 * @Description:
 */
@Data
public class OrderAddrReq {
    private String id;
    private String addrId;

    private OrderAddr orderAddr;
}
