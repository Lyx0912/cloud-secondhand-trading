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
@TableName("ums_member_collect")
public class MemberCollect implements Serializable {

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
     * 物品号
     */
    private Long goodsId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
