package com.xhj.order.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.awt.event.ActionListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: xhj
 * @Date: 2023/04/24/13:42
 * @Description:
 */
@Data
@TableName("oms_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 会员编号
     */
    private Long memberId;
    /**
     * 物品编号
     */
    private Long goodsId;
    /**
     * 订单号
     */
    private String orderSn;
    /**
     * 订单创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 用户名
     */
    private String memberUsername;
    /**
     * 订单总额
     */
    private BigDecimal totalAmount;
    /**
     * 应付总额
     */
    private BigDecimal payAmount;
    /**
     * 支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】
     */
    private Integer payType;
    /**
     * 订单来源[0->PC订单；1->app订单]
     */
    private Integer sourceType;
    /**
     * 订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】
     */
    private Integer state;
    /**
     * 物流公司(配送方式)
     */
    private String deliveryCompany;
    /**
     * 物流单号
     */
    private String deliverySn;
    /**
     * 订单备注
     */
    private String note;
    /**
     * 确认收货状态[0->未确认；1->已确认]
     */
    private Integer confirmStatus;
    /**
     * 删除状态【0->未删除；1->已删除】
     */
    private Integer deleteStatus;
    /**
     * 付款时间
     */
    @DateTimeFormat("yyyy-MM-dd hh:mm:ss")
    private LocalDateTime payTime;
    /**
     * 发货时间
     */
    @DateTimeFormat("yyyy-MM-dd hh:mm:ss")
    private LocalDateTime deliveryTime;
    /**
     * 完成时间
     */
    @DateTimeFormat("yyyy-MM-dd hh:mm:ss")
    private LocalDateTime finallyTime;
    /**
     * 结算时间
     */
    @DateTimeFormat("yyyy-MM-dd hh:mm:ss")
    private LocalDateTime settledTime;
    /**
     * 是否已支付，1.已支付0.未支付
     */
    private Integer isPayed;
    /**
     * 订单关闭原因 1-超时未支付 4-买家取消 15-已通过货到付款交易
     */
    private Integer closeType;
    /**
     * 订单版本号，每处理一次订单，版本号+1
     */
    private Integer version;
    /**
     * 用户订单地址id
     */
    private String orderAddrId;
}
