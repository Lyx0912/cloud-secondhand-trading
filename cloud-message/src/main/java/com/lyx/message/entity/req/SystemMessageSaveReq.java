package com.lyx.message.entity.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author 黎勇炫
 * @date 2023年04月19日 19:05
 */
@Data
public class SystemMessageSaveReq {

    /**
     * 消息ID
     */
    private Long id;

    /**
     * 消息标题
     */
    @NotEmpty(message = "标题不能为空")
    private String title;

    /**
     * 消息内容
     */
    @NotEmpty(message = "内容不能为空")
    private String content;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    private Integer status;

    /**
     * 发布者
     */
    private String creater;

}
