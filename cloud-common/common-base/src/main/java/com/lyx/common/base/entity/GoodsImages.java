package com.lyx.common.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodsImages implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long goodsId;

    private String url;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer isDefault;

}
