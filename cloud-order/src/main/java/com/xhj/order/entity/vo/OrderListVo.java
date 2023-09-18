package com.xhj.order.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lyx.common.base.deserializer.LocalDateTimeDeserializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: xhj
 * @Date: 2023/09/11/15:38
 * @Description:
 */
@Data
public class OrderListVo {
    /**
     * 商品id
     */
    private Long id;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 分类
     */
    private String categoryName;
    /**
     * 卖家
     */
    private String seller;
    /**
     * 买家
     */
    private String buyer;
    /**
     * 订单号
     */
    private String orderSn;
    /**
     * 应付总额
     */
    private BigDecimal payAmount;
    /**
     * 订单状态
     */
    private Integer state;
    /**
     * 购买时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createTime;
    /**
     * 付款时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime payTime;
    /**
     * 发货时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime deliveryTime;
    /**
     * 成交时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime finallyTime;
    /**
     * 图片
     */
    private String defaultImg;
}
