package com.xhj.storage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * AT transaction mode undo table
 * 
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-09-10 17:08:18
 */
@Data
@TableName("undo_log")
public class Log implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * branch transaction id
	 */
	@TableId
	private Long branchId;
	/**
	 * global transaction id
	 */
	private String xid;
	/**
	 * undo_log context,such as serialization
	 */
	private String context;
	/**
	 * rollback info
	 */
	private byte rollbackInfo;
	/**
	 * 0:normal status,1:defense status
	 */
	private Integer logStatus;
	/**
	 * create datetime
	 */
	private Date logCreated;
	/**
	 * modify datetime
	 */
	private Date logModified;

}
