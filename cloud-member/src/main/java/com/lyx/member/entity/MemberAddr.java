package com.lyx.member.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import converter.StatusConverter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

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
    @ColumnWidth(20)
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
    @ColumnWidth(30)
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
    @ColumnWidth(15)
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getAddrId() {
        return addrId;
    }

    public void setAddrId(Long addrId) {
        this.addrId = addrId;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
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

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }
}
