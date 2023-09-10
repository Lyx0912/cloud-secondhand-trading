package com.xhj.order.entity.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.lyx.common.base.entity.dto.GoodsDTO;
import com.lyx.common.base.entity.dto.GoodsEsDTO;
import com.xhj.order.entity.OrderAddr;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/04/24/14:29
 * @Description:
 */
@Data
public class OrderVo {
    private GoodsDTO goodsDTO;
    private List<OrderAddr> orderAddr;
}
