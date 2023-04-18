package com.lyx.goods.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lyx.goods.entity.GoodsDetails;
import com.lyx.goods.entity.GoodsImages;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/04/18/16:22
 * @Description:
 */
@Data
public class AuditVo {
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
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createTime;

    /**
     * 默认的图片地址
     */
    private String defaultImg;

    /**
     * 图片列表
     */
    private List<GoodsImages> images;

    /**
     * 商品详情
     */
    private GoodsDetails details;

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
     * 失败原因
     */
    private String mark;

    /**
     * 审核状态
     */
    private Long state;

    /**
     * 是否上架
     */
    private Integer isOnSell;
}
