package com.xhj.order.mapper;

import com.xhj.order.entity.UndoLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * AT transaction mode undo table
 * 
 * @Author xhj
 * @date 2023-04-24 14:00:41
 * @Description:
 */
@Mapper
public interface UndoLogMapper extends BaseMapper<UndoLog> {
	
}
