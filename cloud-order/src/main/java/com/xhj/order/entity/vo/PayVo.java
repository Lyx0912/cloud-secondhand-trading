package com.xhj.order.entity.vo;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: xhj
 * @Date: 2023/09/26/18:18
 * @Description:
 */
@Data
public class PayVo {
    private Long id;

    /**
     * 创建时间
     */
    @DateTimeFormat("yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createTime;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 支付流水号
     */
    private String bizPayNo;

    /**
     * 支付金额
     */
    private String payAmount;

    /**
     * 卖家名称
     */
    private String sellerName;

    /**
     * 买家名称
     */
    private String buyerName;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 支付状态
     */
    private Long payStatus;
    /**
     * 支付方式
     */
    private Integer payId;

    private static final long serialVersionUID = 1L;
}
