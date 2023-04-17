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
 * 私信表
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-17 03:06:00
 */
@Getter
@Setter
@TableName("mms_private_message")
public class PrivateMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 私信ID
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发送方的用户ID
     */
    private Integer senderId;

    /**
     * 接收方的用户ID
     */
    private Integer receiverId;

    /**
     * 私信内容
     */
    private String content;

    /**
     * 私信创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否已读，0未读，1已读
     */
    private Boolean isRead;


}
