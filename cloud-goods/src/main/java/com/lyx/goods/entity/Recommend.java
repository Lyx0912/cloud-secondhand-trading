package com.lyx.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-04-15 02:19:14
 */
@Getter
@Setter
@TableName("gms_recommend")
public class Recommend implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer goodsId;

    private String goodsName;

    private BigDecimal goodsPrice;

    private String goodsImage;

    private String goodsDescription;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
