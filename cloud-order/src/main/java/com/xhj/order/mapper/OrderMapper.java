package com.xhj.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhj.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/04/24/13:43
 * @Description:
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    List<Order> getOrderList();

}
