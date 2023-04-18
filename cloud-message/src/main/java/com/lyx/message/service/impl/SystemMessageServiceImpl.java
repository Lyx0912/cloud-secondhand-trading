package com.lyx.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.message.config.MessageMapStruct;
import com.lyx.message.entity.SystemMessage;
import com.lyx.message.entity.req.SystemMessageListPageReq;
import com.lyx.message.entity.vo.SystemMessageVO;
import com.lyx.message.mapper.SystemMessageMapper;
import com.lyx.message.service.SystemMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统消息表 服务实现类
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-17 03:06:01
 */
@Service
public class SystemMessageServiceImpl extends ServiceImpl<SystemMessageMapper, SystemMessage> implements SystemMessageService {

    @Autowired
    private MessageMapStruct messageMapStruct;

    /**
     * 分页查询系统消息
     *
     * @param req
     */
    @Override
    public PageUtils<SystemMessageVO> listPage(SystemMessageListPageReq req) {
        Page<SystemMessage> page = new Page<>(req.getPageNo(), req.getPageSize());
        LambdaQueryWrapper<SystemMessage> wrapper = Wrappers.lambdaQuery();
        wrapper.like(!StringUtils.isEmpty(req.getTitle()),SystemMessage::getTitle,req.getTitle())
                .like(!StringUtils.isEmpty(req.getCreater()),SystemMessage::getCreater,req.getCreater());

        this.baseMapper.selectPage(page,wrapper);

        List<SystemMessageVO> systemMessageVOS = messageMapStruct.systemMessageToVO(page.getRecords());
        PageUtils pageUtils = PageUtils.build(page);
        pageUtils.setList(systemMessageVOS);
        return pageUtils;
    }
}
