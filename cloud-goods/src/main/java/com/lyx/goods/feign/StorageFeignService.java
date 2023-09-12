package com.lyx.goods.feign;

import com.lyx.common.base.entity.dto.StorageDTO;
import com.lyx.common.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: xhj
 * @Date: 2023/09/10/22:51
 * @Description:
 */
@FeignClient("cloud-storage")
public interface StorageFeignService {

    @GetMapping("/storage/residue/{id}")
    Integer residue(@PathVariable("id") Long id);

    @GetMapping("/storage/residueGoodsId/{id}")
    Integer residueGoodsId(@PathVariable("id") Long id);

    @RequestMapping("/storage/save")
    R save(@RequestBody StorageDTO storageDTO);
}
