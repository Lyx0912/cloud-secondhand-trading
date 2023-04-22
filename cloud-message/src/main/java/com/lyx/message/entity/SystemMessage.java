package com.lyx.message.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.*;
import converter.StatusConverter;
import lombok.Data;

/**
 * <p>
 * 系统消息表
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-17 03:06:01
 */
@Data
@TableName("mms_system_message")
public class SystemMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty("编号")
    private Long id;

    /**
     * 消息标题
     */
    @ExcelProperty("标题")
    private String title;

    /**
     * 消息内容
     */
    @ExcelProperty("内容")
    private String content;

    /**
     * 消息创建时间
     */
    @TableField(value = "CREATE_TIME",fill = FieldFill.INSERT)
    @DateTimeFormat("yyyy-MM-dd hh:mm:ss")
    @ExcelProperty("发布时间")
    @ColumnWidth(20)
    private LocalDateTime createTime;

     /**
       * 更新时间
       */
    @ExcelIgnore
    private LocalDateTime updateTime;

     /**
       * 状态
       */
    @ExcelProperty(value = "状态",converter = StatusConverter.class)
    private Integer status;

     /**
       * 是否删除
       */
    @TableLogic(value = "0",delval = "1")
    @ExcelIgnore
    private Integer deleted;

     /**
       * 发布者
       */
    @ExcelProperty("发布者")
    private String creater;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }
}
