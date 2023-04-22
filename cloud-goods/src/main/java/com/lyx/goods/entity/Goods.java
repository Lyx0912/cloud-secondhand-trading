package com.lyx.goods.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import converter.StatusConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@ToString
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
    @ExcelProperty("商品名称")
    @ColumnWidth(15)
    private String name;

     /**
       * 卖家
       */
    @ExcelProperty("卖家")
    @ColumnWidth(15)
    private String seller;

    /**
     * 描述
     */
    @ExcelProperty("商品描述")
    @ColumnWidth(20)
    private String description;

    /**
     * 价格
     */
    @ExcelProperty("价格")
    private BigDecimal price;

    /**
     * 卖家
     */
    @ExcelIgnore
    private Integer sellerId;

    /**
     * 浏览量
     */
    @ExcelProperty("浏览量")
    private Integer viewCount;

    /**
     * 是否上架
     */
    @ExcelProperty(value = "上架状态",converter = StatusConverter.class)
    private Integer isOnSell;

    /**
     * 发布时间
     */
    @ExcelProperty("发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @ColumnWidth(20)
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @ExcelIgnore
    private LocalDateTime updateTime;

     /**
       * 是否删除
       */
    @TableLogic(value = "0", delval = "1")
    @ExcelIgnore
    private Integer deleted;

     /**
       * 所属分类
       */
    @ExcelIgnore
    private Long cid;
    /**
     * 默认图片(首页检索展示图)
     */
    @ExcelProperty("默认图片")
    @ColumnWidth(120)
    private String defaultImg;


}
