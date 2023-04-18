package com.lyx.message.mapper;

import com.lyx.message.entity.CommentReply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 评论回复表 Mapper 接口
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-17 03:06:00
 */
@Mapper
public interface CommentReplyMapper extends BaseMapper<CommentReply> {

}
