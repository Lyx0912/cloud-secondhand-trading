package com.lyx.goods.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-31 11:16:28
 */
@Getter
@Setter
@TableName("gms_goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 卖家
     */
    private Integer sellerId;

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
       * 是否删除
       */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

     /**
       * 所属分类
       */
    private Long cid;


}
