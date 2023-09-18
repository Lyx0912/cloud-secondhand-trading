package com.xhj.storage.controller;

import java.util.Arrays;
import java.util.Map;

import com.lyx.common.base.entity.dto.StorageDTO;
import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.storage.entity.req.StorageReq;
import com.xhj.storage.entity.vo.StorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xhj.storage.entity.Storage;
import com.xhj.storage.service.StorageService;



/**
 * 
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-09-10 17:08:18
 */
@RestController
@RequestMapping("/storage")
public class StorageController {
    @Autowired
    private StorageService StorageService;

    /**
     * 分页查询所有库存
     */
    @RequestMapping("/list")
        public R list(StorageReq req){
        PageUtils<StorageVo> pageUtils = StorageService.queryPage(req);

        return R.ok( pageUtils);
    }


    /**
     * 查询库存
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		Storage storage = StorageService.getById(id);
        return R.ok(storage);
    }

    /**
     * 查询剩余库存
     */
    @GetMapping("/residue/{id}")
    public Integer residue(@PathVariable("id") Long id){
        Integer residue = StorageService.getByIdResidue(id);
        return residue;
    }

    /**
     * 查询总库存
     */
    @GetMapping("/residueGoodsId/{id}")
    public Integer residueGoodsId(@PathVariable("id") Long id){
        Integer residue = StorageService.getByIdResidueGoodsId(id);
        return residue;
    }

    /**
     * 保存库存
     */
    @RequestMapping("/save")
    public R save(@RequestBody StorageDTO storageDTO){
        Storage storage = new Storage();
        BeanUtils.copyProperties(storageDTO,storage);
		StorageService.save(storage);

        return R.ok();
    }

    /**
     * 修改库存
     */
    @RequestMapping("/update")
    public R update(@RequestBody Storage demoStorage){
		StorageService.updateById(demoStorage);
        return R.ok();
    }

    /**
     * 减剩余库存 加已用库存
     * @param goodsId
     */
    @RequestMapping("/updateStorage/{id}")
    public R updateStorage(@PathVariable("id") Long goodsId){
		StorageService.updateStorage(goodsId);
        return R.ok();
    }

    /**
     * 加剩余库存 减已用库存
     * @param goodsId
     */
    @RequestMapping("/addStorageTotal/{id}")
    public R addStorageTotal(@PathVariable("id") Long goodsId){
		StorageService.addStorageTotal(goodsId);
        return R.ok();
    }

    /**
     * 删除库存
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		StorageService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
