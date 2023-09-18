package com.xhj.order.feign;

import com.lyx.common.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: xhj
 * @Date: 2023/09/17/0:13
 * @Description:
 */
@FeignClient("cloud-storage")
public interface StorageFeignService {

    @RequestMapping("/storage/updateStorage/{id}")
    R updateStorage(@PathVariable("id") Long goodsId);

    @RequestMapping("/storage/addStorageTotal/{id}")
    R addStorageTotal(@PathVariable("id") Long goodsId);

}
