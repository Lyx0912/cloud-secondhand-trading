package com.lyx.search.entity.req;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

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
    private List<Long> cid;

    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 物品名称
     */
    private String name;
}
