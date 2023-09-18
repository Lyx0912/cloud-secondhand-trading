package com.xhj.order.feign;

import com.lyx.common.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: xhj
 * @Date: 2023/09/15/18:48
 * @Description:
 */
@FeignClient("cloud-member")
public interface MemberFeignService {

    @GetMapping("/memberAddr/{id}")
    R getById(@PathVariable Long id);
}
