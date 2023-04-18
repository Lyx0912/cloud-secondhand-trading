package com.lyx.goods.entity.req;

import com.lyx.common.base.entity.PageReq;
import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/04/18/19:30
 * @Description:
 */
@Data
public class AuditListPageReq extends PageReq {

    // 商品名称
    private String name;
    // 审核状态
    private Long state;
    // 卖家
    private String seller;
}
