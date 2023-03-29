package com.lyx.member.entity.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author 黎勇炫
 * @date 2023年03月29日 21:17
 */
@Data
public class SaveMemberAddrReq {


    /**
     * 用户ID
     */
    @NotNull(message = "会员编号不能为空")
    private Long memberId;

    /**
     * 手机
     */
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    /**
     * 是否默认地址 1是
     */
    private Integer isDefault = 0;

    /**
     * 收货人
     */
    @NotBlank(message = "收货人不能为空")
    private String consignee;

    /**
     * 省ID
     */
    @NotNull(message = "省份编号不能为空")
    private Long provinceId;

    /**
     * 省
     */
    @NotBlank(message = "省份不能为空")
    private String province;

    /**
     * 城市ID
     */
    @NotNull(message = "城市编号不能为空")
    private Long cityId;

    /**
     * 城市
     */
    @NotBlank(message = "城市不能为空")
    private String city;

    /**
     * 区ID
     */
    @NotNull(message = "区编号不能为空")
    private Long areaId;

    /**
     * 区
     */
    @NotBlank(message = "区不能为空")
    private String area;

    /**
     * 邮编
     */
    @NotBlank(message = "邮编不能为空")
    private String postCode;

    /**
     * 地址
     */
    @NotBlank(message = "详细地址不能为空")
    private String addr;

    /**
     * 经度
     */
    @NotNull(message = "经度不能为空")
    private BigDecimal lng;

    /**
     * 纬度
     */
    @NotNull(message = "纬度不能为空")
    private BigDecimal lat;


}
