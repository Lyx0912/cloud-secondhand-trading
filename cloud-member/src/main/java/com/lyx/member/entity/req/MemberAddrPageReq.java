package com.lyx.member.entity.req;

import com.lyx.common.base.entity.PageReq;
import lombok.Data;

/**
 * @author 黎勇炫
 * @date 2023年03月29日 11:43
 */
@Data
public class MemberAddrPageReq extends PageReq {

    private Long memberId;
}
