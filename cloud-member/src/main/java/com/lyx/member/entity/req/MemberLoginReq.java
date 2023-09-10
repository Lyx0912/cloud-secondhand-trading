package com.lyx.member.entity.req;

import lombok.Data;
import lombok.ToString;

/**
 * @Author: xhj
 * @Date: 2023/09/06/14:43
 * @Description:
 */
@Data
@ToString
public class MemberLoginReq {

    private String username;
    private String password;
    private String address;
}
