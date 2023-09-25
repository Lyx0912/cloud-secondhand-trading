package com.xhj.order.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: xhj
 * @Date: 2023/09/25/21:43
 * @Description: 入账记录
 */
@Data
public class OrderRecordedVo {
    /**
     * 订单号
     */
    @TableField(value = "order_sn")
    private String OrderSn;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品id
     */
    @TableField(value = "goods_id")
    private String goodsId;
    /**
     * 买家
     */
    @TableField(value = "member_username")
    private String memberUsername;
    /**
     * 入账金额
     */
    @TableField(value = "pay_amount")
    private BigDecimal payAmount;
    /**
     * 入账时间
     */
    @TableField(value = "finally_time")
    private LocalDateTime finallyTime;
}
