package com.xhj.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.storage.entity.Storage;
import com.xhj.storage.entity.req.LogReq;
import com.xhj.storage.entity.req.StorageReq;
import com.xhj.storage.entity.vo.LogVo;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xhj.storage.dao.LogDao;
import com.xhj.storage.entity.Log;
import com.xhj.storage.service.LogService;


@Service
public class LogServiceImpl extends ServiceImpl<LogDao, Log> implements LogService {

    @Override
    public PageUtils<Log> queryPage(LogReq req) {
        Page<Log> page = new Page<>(req.getPageNo(),req.getPageSize());
        LambdaQueryWrapper<Log> wrapper = Wrappers.lambdaQuery();
        this.baseMapper.selectPage(page, wrapper);
        return PageUtils.build(page);
    }

}