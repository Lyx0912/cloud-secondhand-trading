package com.lyx.member.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-25 09:42:34
 */
@Getter
@Setter
@TableName("ums_login_log")
public class LoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 会员号
     */
    private Long memberId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 登陆类型【1-web,2-app】
     */
    private Boolean loginType;


}
