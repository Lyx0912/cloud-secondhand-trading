package com.lyx.member.config;

import com.lyx.member.entity.Member;
import com.lyx.member.entity.MemberAddr;
import com.lyx.member.entity.vo.MemberAddrVO;
import com.lyx.member.entity.vo.MemberVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author 黎勇炫
 * @date 2023年03月26日 12:11
 */
@Mapper(componentModel="spring")
public interface MemberMapStruct {

    List<MemberVO> memberToMemberVO(List<Member> members);

    List<MemberAddrVO> memberAddrToMemberAddrVO(List<MemberAddr> records);
}
