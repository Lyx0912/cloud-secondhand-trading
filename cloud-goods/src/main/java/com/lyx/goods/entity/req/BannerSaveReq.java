package com.lyx.goods.entity.req;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author 黎勇炫
 * @date 2023年04月13日 10:37
 */
@Data
public class BannerSaveReq {

     /**
       * 编号
       */
    private Long id;

    /**
     * 标题
     */
    @NotEmpty(message = "标题不能为空")
    private String title;

    /**
     * 描述
     */
    @NotEmpty(message = "描述不能为空")
    private String description;

    /**
     * 图片地址
     */
    @NotEmpty(message = "图片地址不能为空")
    private String imageUrl;

    /**
     * 目标跳转地址
     */
    @NotEmpty(message = "目标跳转地址不能为空")
    private String targetUrl;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    private Integer sort;

    /**
     * 激活状态
     */
    @NotNull(message = "请选择激活状态")
    private Integer isActive;

}
