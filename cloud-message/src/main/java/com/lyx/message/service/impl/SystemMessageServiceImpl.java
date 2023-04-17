package com.lyx.message.service.impl;

import com.lyx.message.entity.SystemMessage;
import com.lyx.message.mapper.SystemMessageMapper;
import com.lyx.message.service.SystemMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
