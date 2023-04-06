package com.lyx.member.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.common.base.exception.BizException;
import com.lyx.common.base.result.ResultCode;
import com.lyx.member.config.MemberMapStruct;
import com.lyx.member.entity.Member;
import com.lyx.member.entity.MemberAddr;
import com.lyx.member.entity.req.MemberListPageReq;
import com.lyx.member.entity.vo.MemberVO;
import com.lyx.member.mapper.MemberMapper;
import com.lyx.member.service.MemberAddrService;
import com.lyx.member.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.member.utils.MobileEncrypt;
import org.springframework.beans.BeanUtils;
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
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {


    @Autowired
    private MemberMapStruct memberMapStruct;
    @Autowired
    private MemberAddrService memberAddrService;

    @Override
    public PageUtils<MemberVO> pageMember(MemberListPageReq req) {
        // 构建分页对象 设置分页参数
        Page<Member> page = new Page<>(req.getPageNo(),req.getPageSize());
        // 构建查询条件
        LambdaQueryWrapper<Member> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotEmpty(req.getUsername()),Member::getUsername,req.getUsername())
                .like(StringUtils.isNotEmpty(req.getNickname()),Member::getNickname,req.getNickname())
                .like(StringUtils.isNotEmpty(req.getMobile()),Member::getMobile,req.getMobile())
                .like(StringUtils.isNotEmpty(req.getEmail()),Member::getEmail,req.getEmail())
                .orderByAsc(Member::getId);
        baseMapper.selectPage(page,wrapper);
        PageUtils<MemberVO> pageUtils = new PageUtils<>();
        // 手机号加密
        page.getRecords().forEach(item->{
            item.setMobile(MobileEncrypt.encrypt(item.getMobile()));
        });
        // 转换vo
        pageUtils.setList(memberMapStruct.memberToMemberVO(page.getRecords()));
        pageUtils.setPageNo(page.getCurrent());
        pageUtils.setTotal(page.getTotal());
        // 返回分页对象
        return pageUtils;
    }

    /**
     * 会员详情
     *
     * @param id
     */
    @Override
    public MemberVO getMemberVO(Long id) {
        MemberVO vo = new MemberVO();
        // 查询会员
        Member byId = getById(id);
        if(null == byId){
            throw new BizException(ResultCode.USER_NOT_EXIST);
        }
        BeanUtils.copyProperties(byId,vo);
        vo.setMobile(MobileEncrypt.encrypt(vo.getMobile()));
        // 设置默认收获地址
        LambdaQueryWrapper<MemberAddr> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MemberAddr::getMemberId,id).eq(MemberAddr::getIsDefault,1);
        vo.setMemberAddr(memberAddrService.getOne(wrapper));
        return vo;
    }
}
