package com.lyx.member.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import converter.StatusConverter;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户地址
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-25 09:42:34
 */
@Getter
@Setter
@TableName("ums_member_addr")
public class MemberAddr implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员编号
     */
    @ExcelProperty("会员编号")
    private Long memberId;

    /**
     * 收货人
     */
    @ExcelProperty("收货人")
    private String consignee;

    /**
     * 手机
     */
    @ExcelProperty("手机号")
    private String mobile;

    /**
     * ID
     */
    @TableId(value = "addr_id", type = IdType.AUTO)
    @ExcelIgnore
    private Long addrId;

    /**
     * 省ID
     */
    @ExcelProperty("省编号")
    private Long provinceId;

    /**
     * 省
     */
    @ExcelProperty("省份")
    private String province;

    /**
     * 城市ID
     */
    @ExcelProperty("市编号")
    private Long cityId;

    /**
     * 城市
     */
    @ExcelProperty("市")
    private String city;

    /**
     * 区ID
     */
    @ExcelProperty("区编号")
    private Long areaId;

    /**
     * 区
     */
    @ExcelProperty("区名")
    private String area;

    /**
     * 地址
     */
    @ExcelProperty("详细地址")
    private String addr;

    /**
     * 邮编
     */
    @ExcelProperty("邮编")
    private String postCode;

    /**
     * 是否默认地址 1是
     */
    @ExcelProperty(value = "是否默认", converter = StatusConverter.class)
    private Integer isDefault;

    /**
     * 建立时间
     */
    @TableField(value = "CREATE_TIME",fill = FieldFill.INSERT)
    @ExcelIgnore
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME",fill = FieldFill.INSERT_UPDATE)
    @ExcelIgnore
    private LocalDateTime updateTime;

    /**
     * 经度
     */
    @ExcelIgnore
    private BigDecimal lng;

    /**
     * 纬度
     */
    @ExcelIgnore
    private BigDecimal lat;
}
