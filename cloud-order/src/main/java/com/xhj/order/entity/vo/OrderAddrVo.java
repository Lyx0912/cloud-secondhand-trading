package com.xhj.order.entity.vo;

import com.lyx.common.base.entity.dto.MemberAddrDTO;
import com.xhj.order.entity.Order;
import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/09/07/15:16
 * @Description:
 */
@Data
public class OrderAddrVo {
    private static final long serialVersionUID = 1L;
    /**
     * 订单编号
     */
    private String id;
    /**
     * 商品id
     */
    private Long goodsId;
    private MemberAddrDTO memberAddrDTO;
}
