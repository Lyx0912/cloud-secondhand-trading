package com.xhj.order.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *  xhj
 * @TableName oms_pay
 */
@Data
@TableName(value = "oms_pay")
public class Pay implements Serializable {
    /**
     * 支付id
     */
    @TableId(value = "id",type = IdType.AUTO)
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

    private static final long serialVersionUID = 1L;
}