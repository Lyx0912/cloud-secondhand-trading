package com.lyx.common.base.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/09/24/15:58
 * @Description:
 */
@Data
public class OrderGoodsDTO {
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

    /**
     * 发布时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 默认的图片地址
     */
    private String defaultImg;


    /**
     * 所属分类
     */
    private Long cid;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 商品所属分类路径
     */
    private Long[] categoryPath;

    /**
     * 商品库存
     */
    private Integer total;
}
