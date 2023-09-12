package com.xhj.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.storage.entity.req.StorageReq;
import org.springframework.stereotype.Service;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xhj.storage.dao.StorageDao;
import com.xhj.storage.entity.Storage;
import com.xhj.storage.service.StorageService;
import org.springframework.util.StringUtils;


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

}