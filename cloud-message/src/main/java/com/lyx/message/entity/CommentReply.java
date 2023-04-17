package com.lyx.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 评论回复表
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-17 03:06:00
 */
@Getter
@Setter
@TableName("mms_comment_reply")
public class CommentReply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论回复ID
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评论的用户ID
     */
    private Integer memberId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论创建时间
     */
    private LocalDateTime createTime;

    /**
     * 商品ID
     */
    private Integer itemId;

    /**
     * 回复的父评论ID，可为空
     */
    private Integer parentId;


}
