package com.lyx.goods.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: xhj
 * @Date: 2023/04/17/14:36
 * @Description:
 */
@Data
@TableName("gms_goods_audit")
public class Audit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelIgnore
    private Long id;

    /**
     * 商品id
     */
    @ExcelIgnore
    private Long goodsId;

    /**
     * 失败原因
     */
    @ExcelIgnore
    private String mark;

    /**
     * 审核状态
     */
    @ExcelIgnore
    private Long state;

    /**
     * 是否删除
     */
    @TableLogic(value = "0", delval = "1")
    @ExcelIgnore
    private Integer deleted;

}
