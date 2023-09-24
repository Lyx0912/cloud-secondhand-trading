package com.xhj.order.entity.vo;

import com.lyx.common.base.entity.dto.GoodsEsDTO;
import com.lyx.common.base.entity.dto.OrderGoodsDTO;
import com.xhj.order.entity.Order;
import com.xhj.order.entity.OrderAddr;
import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/09/24/15:29
 * @Description:
 */
@Data
public class OrderInfoVo {
    private OrderGoodsDTO orderGoodsDTO;
    private Order order;
    private OrderAddr orderAddr;
}
