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
 * 系统消息表
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-17 03:06:01
 */
@Getter
@Setter
@TableName("mms_system_message")
public class SystemMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 接收消息的用户ID
     */
    private Integer receiverId;

    /**
     * 消息创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否已读，0未读，1已读
     */
    private Boolean isRead;


}
