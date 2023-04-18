package com.lyx.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    private Long id;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 失败原因
     */
    private String cause;

    /**
     * 审核状态
     */
    private Long state;

}
