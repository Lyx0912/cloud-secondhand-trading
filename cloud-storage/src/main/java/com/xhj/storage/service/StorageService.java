package com.xhj.storage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.storage.entity.Storage;
import com.xhj.storage.entity.req.StorageReq;

import java.util.Map;

/**
 * 
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-09-10 17:08:18
 */
public interface StorageService extends IService<Storage> {

    PageUtils queryPage(StorageReq req);
}

