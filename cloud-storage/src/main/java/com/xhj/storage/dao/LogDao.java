package com.xhj.storage.dao;

import com.xhj.storage.entity.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * AT transaction mode undo table
 * 
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-09-10 17:08:18
 */
@Mapper
public interface LogDao extends BaseMapper<Log> {
	
}
