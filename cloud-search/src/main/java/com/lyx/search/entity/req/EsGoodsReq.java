package com.lyx.search.entity.req;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: xhj
 * @Date: 2023/09/20/14:56
 * @Description:
 */
@Data
public class EsGoodsReq {
    /**
     * 所属分类
     */
    private Long cid;

    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 物品名称
     */
    private String name;
}
