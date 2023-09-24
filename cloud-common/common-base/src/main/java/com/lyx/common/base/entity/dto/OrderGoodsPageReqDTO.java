package com.lyx.common.base.entity.dto;

import com.lyx.common.base.entity.PageReq;
import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/09/23/23:37
 * @Description:
 */
@Data
public class OrderGoodsPageReqDTO extends PageReq {
    private String name;
    private String seller;
}
