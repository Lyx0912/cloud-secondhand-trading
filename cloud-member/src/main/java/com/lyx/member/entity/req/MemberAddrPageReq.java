package com.lyx.member.entity.req;

import com.lyx.common.base.entity.PageReq;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 黎勇炫
 * @date 2023年03月29日 11:43
 */
@Data
public class MemberAddrPageReq extends PageReq {

     /**
       * 会员编号
       */
    private Long memberId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 收货人
     */
    private String consignee;
}
