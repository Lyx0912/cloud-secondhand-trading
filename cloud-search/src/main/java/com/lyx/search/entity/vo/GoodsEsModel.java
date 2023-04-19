package com.lyx.search.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: xhj
 * @Date: 2023/04/19/14:55
 * @Description:
 */
@Data
public class GoodsEsModel {


    /**
     * 编号
     */
    private Long id;

    /**
     * 物品名称
     */
    private String name;

    /**
     * 卖家
     */
    private String seller;

    /**
     * 描述
     */
    private String description;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 是否上架
     */
    private Integer isOnSell;

}