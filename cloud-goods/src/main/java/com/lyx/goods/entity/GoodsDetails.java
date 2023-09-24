package com.lyx.goods.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.lyx.common.mp.handler.MyMetaObjectHandler;
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
@TableName("gms_goods_details")
public class GoodsDetails implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long goodsId;

    private String content;

//    @TableField(value = "CREATE_AT" ,fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

//    @TableField(value = "CREATE_AT",fill = FieldFill.UPDATE)
    private LocalDateTime updatedAt;


}
