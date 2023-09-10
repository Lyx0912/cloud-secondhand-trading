package com.xhj.storage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * 
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-09-10 17:08:18
 */
@Data
@TableName("demo_storage")
public class Storage implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 产品id
	 */
	private Long productId;
	/**
	 * 总库存
	 */
	private Integer total;
	/**
	 * 已用库存
	 */
	private Integer used;
	/**
	 * 剩余库存
	 */
	private Integer residue;

}
