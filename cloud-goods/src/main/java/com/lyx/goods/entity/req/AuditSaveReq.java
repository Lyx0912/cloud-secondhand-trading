package com.lyx.goods.entity.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/04/21/17:00
 * @Description:
 */
@Data
public class AuditSaveReq {

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 失败原因
     */
    private String mark;

    /**
     * 审核状态
     */
    private Long state;
}
