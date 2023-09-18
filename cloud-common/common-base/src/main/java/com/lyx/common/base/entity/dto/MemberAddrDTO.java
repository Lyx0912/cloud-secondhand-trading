package com.lyx.common.base.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: xhj
 * @Date: 2023/09/15/18:51
 * @Description:
 */
@Data
public class MemberAddrDTO {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    private Long addrId;

    /**
     * 用户ID
     */
    private Long memberId;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 是否默认地址 1是
     */
    private Integer isDefault;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 省ID
     */
    private Long provinceId;

    /**
     * 省
     */
    private String province;

    /**
     * 城市ID
     */
    private Long cityId;

    /**
     * 城市
     */
    private String city;

    /**
     * 区ID
     */
    private Long areaId;

    /**
     * 区
     */
    private String area;

    /**
     * 邮编
     */
    private String postCode;

    /**
     * 地址
     */
    private String addr;

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 纬度
     */
    private BigDecimal lat;
}
