package com.lyx.goods.entity.req;

import com.lyx.common.base.entity.PageReq;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 黎勇炫
 * @date 2023年04月05日 14:32
 */
@Data
public class GoodsListPageReq extends PageReq {
    // 商品名称
    private String name;
    // 上架状态
    private Integer isOnSell;
    // 卖家
    private String seller;
    // 卖家id
    private Long seller_id;
    // 分类id
    private Long category_id;

}
