package com.xhj.order.entity.vo;

import com.lyx.common.base.entity.dto.OrderGoodsDTO;
import com.xhj.order.entity.Order;
import com.xhj.order.entity.OrderAddr;
import lombok.Data;

import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/09/25/0:45
 * @Description:
 */
@Data
public class ResAddrReq {
    private OrderGoodsDTO goodsDTO;
    private List<OrderAddr> orderAddrs;
}
