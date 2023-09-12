package com.xhj.order.feign;

import com.lyx.common.base.entity.dto.GoodsDTO;
import com.lyx.common.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: xhj
 * @Date: 2023/09/07/14:56
 * @Description:
 */
@FeignClient("cloud-goods")
public interface GoodsFeignService {

    /**
     * 查询商品详情
     */
    @GetMapping("/goods/orderGoods/{id}")
    GoodsDTO orderInfo(@PathVariable Long id);

    /**
     * 查询商品详情
     */
    @GetMapping("/goods/infoFeign/{id}")
    R orderFeignInfo(@PathVariable Long id);

}
