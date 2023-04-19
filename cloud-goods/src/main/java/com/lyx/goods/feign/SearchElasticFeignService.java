package com.lyx.goods.feign;

import com.lyx.common.base.result.R;
import com.lyx.goods.entity.GoodsEsModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/04/19/15:22
 * @Description:
 */
@FeignClient("cloud-search")
public interface SearchElasticFeignService {

    @PostMapping("/search/goods")
    public R goodsStatusUp(@RequestBody List<GoodsEsModel> goodsEsModels);
}
