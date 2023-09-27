package com.xhj.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.storage.entity.req.StorageReq;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xhj.storage.dao.StorageDao;
import com.xhj.storage.entity.Storage;
import com.xhj.storage.service.StorageService;
import org.springframework.transaction.annotation.Transactional;


@Service("StorageServiceImpl")
public class StorageServiceImpl extends ServiceImpl<StorageDao, Storage> implements StorageService {

    @Override
    public PageUtils<Storage> queryPage(StorageReq req) {
        Page<Storage> page = new Page<>(req.getPageNo(),req.getPageSize());
        LambdaQueryWrapper<Storage> wrapper = Wrappers.lambdaQuery();
        this.baseMapper.selectPage(page, wrapper);
        return PageUtils.build(page);
    }

    /**
     * 查询剩余库存
     * @param id
     * @return
     */
    @Override
    public Integer getByIdResidue(Long id) {
        QueryWrapper<Storage> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id",id);
        Storage storage = this.getOne(wrapper);
        if (storage!=null){
            return storage.getResidue();
        }
        return 0;
    }

    /**
     * 查询总库存
     * @param id
     * @return
     */
    @Override
    public Integer getByIdResidueGoodsId(Long id) {
        QueryWrapper<Storage> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id",id);
        Storage storage = this.getOne(wrapper);
        if (storage!=null){
            return storage.getTotal();
        }
        return 0;
    }

    /**
     * 减剩余库存 加已用库存
     * @param goodsId
     */
    @Transactional
    @Override
    public void updateStorage(Long goodsId) {
        LambdaQueryWrapper<Storage> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.eq(true,Storage::getProductId,goodsId);
        Storage storage = this.getOne(lambdaQuery);
        LambdaUpdateWrapper<Storage> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(true,Storage::getResidue,storage.getResidue()==null?0:storage.getResidue()-1)
                .set(true,Storage::getUsed,(storage.getUsed()==null?1:storage.getUsed())+1)
                .eq(true,Storage::getId,goodsId);
        this.update(wrapper);
    }

    /**
     * 加剩余库存 减已用库存
     * @param goodsId
     */
    @Override
    public void addStorageTotal(Long goodsId) {
        LambdaQueryWrapper<Storage> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.eq(true,Storage::getProductId,goodsId);
        Storage storage = this.getOne(lambdaQuery);
//        LambdaUpdateWrapper<Storage> wrapper = Wrappers.lambdaUpdate();
//        wrapper.set(true,Storage::getResidue,storage.getResidue()==null?1:storage.getResidue()+1)
//                .set(true,Storage::getUsed,(storage.getUsed()==null?0:storage.getUsed())-1)
//                .eq(true,Storage::getId,goodsId);
        baseMapper.addUpdate(goodsId);
    }

}