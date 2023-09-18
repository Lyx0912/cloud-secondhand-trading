package com.lyx.member.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.member.config.MemberMapStruct;
import com.lyx.member.entity.MemberAddr;
import com.lyx.member.entity.req.MemberAddrPageReq;
import com.lyx.member.entity.req.SaveMemberAddrReq;
import com.lyx.member.entity.vo.MemberAddrVO;
import com.lyx.member.mapper.MemberAddrMapper;
import com.lyx.member.service.MemberAddrService;
import com.lyx.member.utils.MobileEncrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户地址 服务实现类
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-25 09:42:34
 */
@Service
public class MemberAddrServiceImpl extends ServiceImpl<MemberAddrMapper, MemberAddr> implements MemberAddrService {

    @Autowired
    private MemberMapStruct memberMapStruct;

    /**
     * 分页查询会员的收获地址
     *
     * @param req
     */
    @Override
    public PageUtils<MemberAddrVO> pageMemberAddr(MemberAddrPageReq req) {
        // 构建分页对象 设置分页参数
        Page<MemberAddr> page = new Page<>(req.getPageNo()==0?1:req.getPageNo(),req.getPageSize()==0?5:req.getPageSize());
        // 构建查询条件
        LambdaQueryWrapper<MemberAddr> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(req.getMemberId()!=null,MemberAddr::getMemberId,req.getMemberId())
                .like(StringUtils.isNotEmpty(req.getConsignee()),MemberAddr::getConsignee,req.getConsignee())
                .eq(StringUtils.isNotEmpty(req.getMobile()),MemberAddr::getMobile,req.getMobile())
                .orderByDesc(MemberAddr::getIsDefault);
        baseMapper.selectPage(page,wrapper);
        PageUtils<MemberAddrVO> pageUtils = new PageUtils<>();
        // 手机号加密
        page.getRecords().forEach(item->{
            item.setMobile(MobileEncrypt.encrypt(item.getMobile()));
        });

        // 转换vo
        List<MemberAddrVO> memberAddrVOS = memberMapStruct.memberAddrToMemberAddrVO(page.getRecords());
        if (req.getPageNo()==0){
            memberAddrVOS = memberAddrVOS.stream().filter(item -> item.getIsDefault() == 1).collect(Collectors.toList());
        }
        if (req.getPageSize()==0){
            memberAddrVOS = memberAddrVOS.stream().filter(item -> item.getIsDefault() == 0).collect(Collectors.toList());
        }
        pageUtils.setList(memberAddrVOS);
        pageUtils.setPageNo(page.getCurrent());
        pageUtils.setTotal(page.getTotal());

        // 返回分页对象
        return pageUtils;
    }

    /**
     * 添加收货地址
     *
     * @param req
     */
    @Override
    public void saveMemberAddr(SaveMemberAddrReq req) {
        MemberAddr memberAddr = new MemberAddr();
        // 拷贝属性
        BeanUtils.copyProperties(req,memberAddr);
        save(memberAddr);
    }

    /**
     * 根据id查询收货地址
     * @param id
     * @return
     */
    @Override
    public MemberAddrVO getMemberAddr(Long id) {
        MemberAddr memberAddr = this.getById(id);
        MemberAddrVO memberAddrVO = new MemberAddrVO();
        BeanUtils.copyProperties(memberAddr,memberAddrVO);
        return memberAddrVO;
    }


}
