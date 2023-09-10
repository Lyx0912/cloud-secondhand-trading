package com.xhj.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.storage.entity.req.StorageReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xhj.storage.dao.StorageDao;
import com.xhj.storage.entity.Storage;
import com.xhj.storage.service.StorageService;


@Service("StorageServiceImpl")
public class StorageServiceImpl extends ServiceImpl<StorageDao, Storage> implements StorageService {

    @Override
    public PageUtils<Storage> queryPage(StorageReq req) {
        Page<Storage> page = new Page<>(req.getPageNo(),req.getPageSize());
        LambdaQueryWrapper<Storage> wrapper = Wrappers.lambdaQuery();
        this.baseMapper.selectPage(page, wrapper);
        return PageUtils.build(page);
    }

}