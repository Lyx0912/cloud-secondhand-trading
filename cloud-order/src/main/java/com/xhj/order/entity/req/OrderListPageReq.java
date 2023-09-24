package com.xhj.order.entity.req;

import com.lyx.common.base.entity.PageReq;
import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/04/24/14:29
 * @Description:
 */
@Data
public class OrderListPageReq extends PageReq {
    private Long uid;
    private Long state;
    /**
     * 卖家
     */
    private String seller;
    /**
     * 买家
     */
    private String buyer;
    /**
     * 商品名称
     */
    private String name;

}
