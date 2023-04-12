package com.lyx.goods.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
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
@Data
@TableName("gms_category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty(value = "分类编号")
    @ColumnWidth(20)
    private Long id;

    /**
     * 分类名称
     */
    @ExcelProperty(value = "用户名")
    @ColumnWidth(20)
    private String name;

    /**
     * 父分类id
     */
    @ExcelProperty(value = "父分类编号")
    @ColumnWidth(20)
    private Long parentCid;

    /**
     * 层级
     */
    @ExcelProperty(value = "层级")
    @ColumnWidth(20)
    private Integer catLevel;

    /**
     * 是否显示[0-不显示，1显示]
     */
    @ExcelIgnore
    private Integer showStatus;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    @ColumnWidth(20)
    private Integer sort;

    /**
     * 图标地址
     */
    @ExcelIgnore
    private String icon;

    /**
     * 计量单位
     */
    @ExcelIgnore
    private String productUnit;

    /**
     * 商品数量
     */
    @ExcelProperty(value = "商品数量")
    @ColumnWidth(20)
    private Integer productCount;

     /**
       * 子菜单
       */
    @TableField(exist = false)
    @ExcelIgnore
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Category> childrens;

}
