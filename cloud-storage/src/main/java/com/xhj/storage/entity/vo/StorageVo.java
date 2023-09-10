package com.xhj.storage.entity.vo;

import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/09/10/17:34
 * @Description:
 */
@Data
public class StorageVo {
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
