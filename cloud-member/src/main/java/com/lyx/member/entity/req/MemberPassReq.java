package com.lyx.member.entity.req;

import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/09/20/17:11
 * @Description:
 */
@Data
public class MemberPassReq {
    private Long uid;
    private String name;
    private String password;
    private String oldpassword;
}
