package com.lyx.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyx.goods.entity.Audit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p></p>
 *
 * @Author: xhj
 * @Date: 2023/04/17/14:41
 * @Description:
 */
@Mapper
public interface AuditMapper extends BaseMapper<Audit> {
    Long getAuditById(@Param("id") Long id);
}
