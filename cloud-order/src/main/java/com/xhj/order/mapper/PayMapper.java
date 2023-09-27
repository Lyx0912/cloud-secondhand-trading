package com.xhj.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhj.order.entity.Pay;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: xhj
 * @Date: 2023/09/26/18:08
 * @Description:
 */
@Mapper
public interface PayMapper extends BaseMapper<Pay>  {
    int count();

    void savePay(Pay pay);

    /**
     * 修改支付状态
     * @param i
     */
    void updataState(String orderSn,int i);
}







