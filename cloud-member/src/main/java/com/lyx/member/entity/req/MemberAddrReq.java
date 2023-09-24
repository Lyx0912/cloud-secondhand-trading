package com.lyx.member.entity.req;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import converter.StatusConverter;
import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/09/19/14:00
 * @Description:
 */
@Data
public class MemberAddrReq {
    /**
     * 地址id
     */
    private Long addrId;
    /**
     * 地址
     */
    private String[] addrsPath;

    /**
     * 详细地址
     */
    private String addr;

    /**
     * 是否默认地址 1是
     */
    private Integer isDefault;

    /**
     * 会员编号
     */
    private Long memberId;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 手机
     */
    private String mobile;
}
