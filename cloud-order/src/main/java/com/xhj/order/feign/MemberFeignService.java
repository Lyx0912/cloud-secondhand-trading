package com.xhj.order.feign;

import com.lyx.common.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/09/15/18:48
 * @Description:
 */
@FeignClient("cloud-member")
public interface MemberFeignService {

    @GetMapping("/memberAddr/{id}")
    R getById(@PathVariable Long id);

    /**
     * 查询用户发布商品数量
     */
    @GetMapping("/member/count/{memberId}")
    R count(@PathVariable Long memberId);

    /**
     * 批量查询
     * @param ids
     * @return
     */
    @PostMapping("/getList")
    R getList(@RequestBody List<Long> ids);


}
