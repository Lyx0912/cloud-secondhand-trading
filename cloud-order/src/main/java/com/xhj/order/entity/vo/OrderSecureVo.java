package com.xhj.order.entity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/09/25/21:37
 * @Description:
 */
@Data
public class OrderSecureVo {
    /**
     * 总销售额
     */
    private BigDecimal salesTotal;
    /**
     * 总购买量
     */
    private BigDecimal purchasesTotal;
    /**
     * 入账总金额
     */
    private BigDecimal recordedTotal;
    /**
     * 钱包余额
     */
    private BigDecimal purse;
    /**
     * 入账记录
     */
    private List<OrderRecordedVo> recordedVos;

}
