package com.xhj.order.feign;

import com.lyx.common.base.entity.dto.GoodsDTO;
import com.lyx.common.base.entity.dto.OrderGoodsDTO;
import com.lyx.common.base.entity.dto.OrderGoodsPageReqDTO;
import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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
    @GetMapping("/goods/orderGoodsInfo/{id}")
    OrderGoodsDTO orderGoodsInfo(@PathVariable Long id);

    /**
     * 用户查询商品列表
     */
    @PostMapping("/goods/goodsList")
    List<GoodsDTO> goodsPageList(@RequestBody OrderGoodsPageReqDTO reqDTO);

    /**
     * 查询商品详情
     */
    @GetMapping("/goods/infoFeign/{id}")
    R orderFeignInfo(@PathVariable Long id);

    /**
     * 查询用户发布商品数量
     */
    @GetMapping("/goods/count/{memberId}")
    R count(@PathVariable Long memberId);

    /**
     * 查询用户发布商品id
     */
    @GetMapping("/goods/getGoods/{memberId}")
    List<Long> getGoodsById(@PathVariable Long memberId);

    /**
     * 查询商品列表
     */
    @GetMapping("/goods/list/{ids}")
    List<GoodsDTO> listIds(@PathVariable("ids") List<Long> ids);

}
