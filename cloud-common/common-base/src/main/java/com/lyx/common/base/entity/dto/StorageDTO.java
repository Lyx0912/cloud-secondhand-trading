package com.lyx.common.base.entity.dto;

import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/09/12/19:23
 * @Description:
 */
@Data
public class StorageDTO {
    /**
     *
     */
    private Long id;
    /**
     * 产品id
     */
    private Long productId;
    /**
     * 总库存
     */
    private Integer total;
    /**
     * 已用库存
     */
    private Integer used;
    /**
     * 剩余库存
     */
    private Integer residue;
}
