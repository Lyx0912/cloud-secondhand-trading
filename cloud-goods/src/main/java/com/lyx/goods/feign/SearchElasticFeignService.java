package com.lyx.goods.feign;

import com.lyx.common.base.entity.dto.EsGoodsDTO;
import com.lyx.common.base.entity.dto.GoodsDTO;
import com.lyx.common.base.entity.dto.GoodsEsDTO;
import com.lyx.common.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/04/19/15:22
 * @Description:
 */
@FeignClient("cloud-search")
public interface SearchElasticFeignService {

    @PostMapping("/search/goods")
    R goodsStatusUp(@RequestBody List<GoodsEsDTO> goodsEsDTOS);

    @GetMapping("/search/{ids}")
    R goodsDelete(@PathVariable List<Long> ids);

    @PostMapping("/search/es/goods")
    List<GoodsDTO>  goodsEsList(@RequestBody EsGoodsDTO esGoodsDTO);
}
