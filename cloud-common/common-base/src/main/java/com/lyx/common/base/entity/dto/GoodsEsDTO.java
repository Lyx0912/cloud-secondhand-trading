package com.lyx.common.base.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lyx.common.base.deserializer.LocalDateTimeDeserializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: xhj
 * @Date: 2023/04/20/14:47
 * @Description:
 */
@Data
public class GoodsEsDTO implements Serializable {
    /**
     * 编号
     */
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
     * 发布时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createTime;

    /**
     * 默认的图片地址
     */
    private String defaultImg;

    /**
     * 所属分类
     */
    private Long cid;
}
