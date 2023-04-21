package com.lyx.goods.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@TableName("gms_goods_images")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodsImages implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long goodsId;

    private String url;

    @TableField(value = "CREATE_TIME",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Integer isDefault;

}
