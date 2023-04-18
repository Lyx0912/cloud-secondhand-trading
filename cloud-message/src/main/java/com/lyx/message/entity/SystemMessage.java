package com.lyx.message.entity;

import com.baomidou.mybatisplus.annotation.*;

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
     * 消息创建时间
     */
    private LocalDateTime createTime;

     /**
       * 更新时间
       */
    private LocalDateTime updateTime;

     /**
       * 状态
       */
    private Integer status;

     /**
       * 是否删除
       */
    @TableLogic(value = "0",delval = "1")
    private Integer deleted;

     /**
       * 发布者
       */
    private String creater;


}
