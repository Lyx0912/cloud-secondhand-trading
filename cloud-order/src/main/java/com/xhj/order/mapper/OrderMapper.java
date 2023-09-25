package com.xhj.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhj.order.entity.Order;
import com.xhj.order.entity.vo.OrderRecordedVo;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/04/24/13:43
 * @Description:
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    List<Order> getOrderList();
    /**
     * 查询商品销售额
     * @param
     * @return
     */
    BigDecimal getSalesTotal(List<Long> goodsById);

    /**
     * 总购买量
     * @param goodsById
     * @return
     */
    int getPurchases(List<Long> goodsById);

    /**
     * 入账总金额
     * @param goodsById
     * @return
     */
    BigDecimal getRecordedTotal(List<Long> goodsById);

    /**
     * 入账记录
     * @param goodsById
     * @return
     */
    List<OrderRecordedVo> getOrderRecorded(List<Long> goodsById);
}
