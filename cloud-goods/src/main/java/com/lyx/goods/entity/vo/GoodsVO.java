package com.lyx.goods.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author 黎勇炫
 * @date 2023年04月05日 14:37
 */
@Data
public class GoodsVO {

    /**
     * 物品名称
     */
    private String name;

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
    private String url;
}
