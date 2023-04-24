package com.xhj.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * AT transaction mode undo table
 * 
 * @Author xhj
 * @date 2023-04-24 14:00:41
 * @Description:
 */
@Data
@TableName("undo_log")
public class UndoLog implements Serializable {
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
	private byte[] rollbackInfo;
	/**
	 * 0:normal status,1:defense status
	 */
	private Integer logStatus;
	/**
	 * create datetime
	 */
	private LocalDateTime logCreated;
	/**
	 * modify datetime
	 */
	private LocalDateTime logModified;

}
