package com.lyx.goods.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-13 08:36:46
 */
@Getter
@Setter
@TableName("gms_banner")
public class Banner implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

     /**
       * 标题
       */
    private String title;

     /**
       * 描述
       */
    private String description;

     /**
       * 图片地址
       */
    private String imageUrl;

     /**
       * 目标跳转地址
       */
    private String targetUrl;

     /**
       * 排序
       */
    private Integer sort;

     /**
       * 激活状态
       */
    private Integer isActive;

     /**
       * 更新时间
       */
    @TableField(value = "UPDATE_TIME",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

     /**
       * 发布时间
       */
    @TableField(value = "CREATE_TIME",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

     /**
       * 是否删除
       */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;


}
