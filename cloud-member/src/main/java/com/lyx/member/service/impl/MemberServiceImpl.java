package com.lyx.member.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyx.common.base.entity.PageUtils;
import com.lyx.member.config.MemberMapStruct;
import com.lyx.member.entity.Member;
import com.lyx.member.entity.req.MemberListPageReq;
import com.lyx.member.entity.vo.MemberVO;
import com.lyx.member.mapper.MemberMapper;
import com.lyx.member.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-25 09:39:17
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {


    private final MemberMapStruct memberMapStruct;

    @Override
    public PageUtils<MemberVO> pageMember(MemberListPageReq req) {
        // 构建分页对象 设置分页参数
        Page<Member> page = new Page<>(req.getPageNo(),req.getPageSize());
        // 构建查询条件
        LambdaQueryWrapper<Member> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotEmpty(req.getUsername()),Member::getUsername,req.getUsername())
                .like(StringUtils.isNotEmpty(req.getNickname()),Member::getNickname,req.getNickname())
                .like(StringUtils.isNotEmpty(req.getMobile()),Member::getMobile,req.getMobile())
                .like(StringUtils.isNotEmpty(req.getEmail()),Member::getEmail,req.getEmail());
        baseMapper.selectPage(page,wrapper);
        PageUtils<MemberVO> pageUtils = new PageUtils<>();
        // 转换vo
        pageUtils.setList(memberMapStruct.memberToMemberVO(page.getRecords()));
        pageUtils.setPageNo(page.getCurrent());
        pageUtils.setTotal(page.getTotal());
        // 返回分页对象
        return pageUtils;
    }
}
