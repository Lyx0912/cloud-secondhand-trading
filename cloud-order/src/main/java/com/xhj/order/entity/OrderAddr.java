package com.xhj.order.entity;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户订单配送地址
 * 
 * @Author xhj
 * @date 2023-04-24 14:00:41
 * @Description:
 */
@Data
@TableName("oms_order_addr")
public class OrderAddr implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private String id;
	/**
	 * 创建时间
	 */
	@DateTimeFormat("yyyy-MM-dd hh:mm:ss")
	private LocalDateTime createTime;
	/**
	 * 更新时间
	 */
	@DateTimeFormat("yyyy-MM-dd hh:mm:ss")
	private LocalDateTime updateTime;
	/**
	 * 用户ID
	 */
	private Long memberId;
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
	 * 区域ID
	 */
	private Long areaId;
	/**
	 * 区
	 */
	private String area;
	/**
	 * 地址
	 */
	private String addr;
	/**
	 * 邮编
	 */
	private String postCode;
	/**
	 * 手机
	 */
	private String mobile;
	/**
	 * 经度
	 */
	private BigDecimal lng;
	/**
	 * 纬度
	 */
	private BigDecimal lat;

}
