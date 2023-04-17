package com.lyx.goods.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-15 02:19:14
 */
@Getter
@Setter
@TableName("gms_recommend")
public class Recommend implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("推荐编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

     /**
       * 物品管理
       */
    @ExcelProperty("物品编号")
    private Long goodsId;

     /**
       * 商品名称
       */
     @ExcelProperty("物品名称")
     private String goodsName;

     /**
       * 价格
       */
    @ExcelProperty("价格")
    private BigDecimal goodsPrice;

     /**
       * 商品图片
       */
     @ExcelProperty("图片地址")
     @ColumnWidth(80)
     private String goodsImage;

     /**
       * 商品描述
       */
    @ExcelProperty("描述")
    private String goodsDescription;

     /**
       * 创建时间
       */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    @ExcelProperty("发布时间")
    @ColumnWidth(20)
    private LocalDateTime createTime;

     /**
       * 更新时间
       */
    @ExcelIgnore
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


     /**
       * 是否删除
       */
    @TableLogic(value = "0", delval = "1")
    @ExcelIgnore
    private Integer deleted;


}
