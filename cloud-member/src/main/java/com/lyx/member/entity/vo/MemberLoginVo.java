package com.lyx.member.entity.vo;

import com.lyx.member.entity.Member;
import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/09/06/21:04
 * @Description:
 */
@Data
public class MemberLoginVo {

    private String token;
    private Member member;
    private int id;

}
