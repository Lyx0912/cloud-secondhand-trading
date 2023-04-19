package com.lyx.member.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import converter.GenderConverter;
import converter.LocalDateConverter;
import converter.LocalDateTimeConverter;
import converter.StatusConverter;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-25 09:39:17
 */
@TableName("ums_member")
@Data
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ExcelIgnore
    private Long id;

    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名")
    @ColumnWidth(20)
    private String username;

    /**
     * 密码
     */
    @ExcelIgnore
    private String password;

    /**
     * 昵称
     */
    @ExcelProperty("昵称")
    @ColumnWidth(20)
    private String nickname;

    /**
     * 手机号码
     */
    @ExcelProperty("手机号")
    @ColumnWidth(20)
    private String mobile;

    /**
     * 邮箱
     */
    @ExcelProperty("邮箱")
    @ColumnWidth(20)
    private String email;

    /**
     * 头像
     */
    @ExcelIgnore
    private String avatar;

    /**
     * 性别
     */
    @ExcelProperty(value = "性别", converter = GenderConverter.class)
    @ColumnWidth(10)
    private Integer gender;

    /**
     * 生日
     */
    @ExcelProperty(value = "生日",converter = LocalDateConverter.class)
    @ColumnWidth(15)
    @DateTimeFormat("yyyy-MM-dd")
    private LocalDate birth;

    /**
     * 账号状态
     */
    @ExcelProperty(value = "账号状态",converter = StatusConverter.class)
    @ColumnWidth(10)
    @DateTimeFormat("yyyy-MM-dd")
    private Integer status;

    /**
     * 最近登陆时间
     */
    @ExcelProperty(value = "最近登陆时间",converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd hh:mm:ss")
    private LocalDateTime lastLoginTime;

     /**
       * 删除状态
       */
    @TableLogic(value = "0", delval = "1")
    @ExcelIgnore
    private Integer deleted;

    @TableField(value = "CREATE_TIME",fill = FieldFill.INSERT)
    @DateTimeFormat("yyyy-MM-dd hh:mm:ss")
    @ExcelProperty(value = "注册时间",converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME",fill = FieldFill.INSERT_UPDATE)
    @ExcelIgnore
    private LocalDateTime updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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
}
