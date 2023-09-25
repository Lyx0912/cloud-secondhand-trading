package com.lyx.goods.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.NotNull;

/**
 * @Author: xhj
 * @Date: 2023/09/25/18:17
 * @Description:
 */
@FeignClient("cloud-member")
public interface MemberFeignService {

    /**
     * 获取手机号
     * @param id
     * @return
     */
    @GetMapping("/member/memberMobile/{id}")
    String getMemberMobile(@NotNull(message = "id can not be null!") @PathVariable Long id);

}
