package com.lyx.member.service;

import com.lyx.common.mp.utils.PageUtils;
import com.lyx.member.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyx.member.entity.req.MemberListPageReq;
import com.lyx.member.entity.vo.MemberVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-25 09:39:17
 */
public interface MemberService extends IService<Member> {

     /**
       * 分页查询会员
       */
    PageUtils<MemberVO> pageMember(MemberListPageReq req);

     /**
       * 会员详情
       */
    MemberVO getMemberVO(Long id);
}
