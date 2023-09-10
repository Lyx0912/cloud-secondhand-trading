package com.xhj.storage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.storage.entity.Log;
import com.xhj.storage.entity.req.LogReq;

import java.util.Map;

/**
 * AT transaction mode undo table
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-09-10 17:08:18
 */
public interface LogService extends IService<Log> {

    public PageUtils<Log> queryPage(LogReq req) ;
}

