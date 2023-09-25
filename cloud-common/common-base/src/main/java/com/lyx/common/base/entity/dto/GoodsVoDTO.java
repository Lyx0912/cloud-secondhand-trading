package com.lyx.common.base.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lyx.common.base.entity.GoodsImages;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/09/25/14:44
 * @Description:
 */
@Data
public class GoodsVoDTO {
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
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 默认的图片地址
     */
    private String defaultImg;

    /**
     * 图片列表
     */
    private List<GoodsImages> images;


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
