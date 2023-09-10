package com.lyx.goods.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lyx.goods.entity.Category;
import com.lyx.goods.entity.GoodsDetails;
import com.lyx.goods.entity.GoodsImages;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 黎勇炫
 * @date 2023年04月05日 14:37
 */
@Data
public class GoodsVO {


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
     * 商品库存
     */
    private Long total;
}
