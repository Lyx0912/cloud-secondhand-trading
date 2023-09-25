package com.xhj.order.entity.vo;

import com.lyx.common.base.entity.dto.GoodsVoDTO;
import com.lyx.common.base.entity.dto.OrderGoodsDTO;
import lombok.Data;

import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/09/25/1:42
 * @Description:
 */
@Data
public class OrderAddrMemberIdVo {
    private GoodsVoDTO goodsDTO;
    private List<OrderInfoVo> orderInfoVos;
}
