package com.lyx.member.service;

import com.lyx.common.mp.utils.PageUtils;
import com.lyx.member.entity.MemberAddr;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyx.member.entity.req.MemberAddrPageReq;
import com.lyx.member.entity.req.MemberAddrReq;
import com.lyx.member.entity.req.SaveMemberAddrReq;
import com.lyx.member.entity.vo.MemberAddrOneVo;
import com.lyx.member.entity.vo.MemberAddrVO;

/**
 * <p>
 * 用户地址 服务类
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-25 09:42:34
 */
public interface MemberAddrService extends IService<MemberAddr> {

     /**
       * 分页查询会员的收获地址
       */
    PageUtils<MemberAddrVO> pageMemberAddr(MemberAddrPageReq req);

     /**
       * 添加收货地址
       */
    void saveMemberAddr(SaveMemberAddrReq req);

    /**
     * 根据id查询收货地址
     * @param id
     * @return
     */
    MemberAddrOneVo getMemberAddr(Long id);

    /**
     * 用户添加收货地址
     * @param req
     */
    void saveMemberAddrs(MemberAddrReq req);

    /**
     * 修改地址
     * @param req
     */
    MemberAddr updateMemberAddr(MemberAddrReq req);
}
