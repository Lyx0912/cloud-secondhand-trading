package com.lyx.message.service.impl;

import com.lyx.message.entity.PrivateMessage;
import com.lyx.message.mapper.PrivateMessageMapper;
import com.lyx.message.service.PrivateMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 私信表 服务实现类
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-17 03:06:00
 */
@Service
public class PrivateMessageServiceImpl extends ServiceImpl<PrivateMessageMapper, PrivateMessage> implements PrivateMessageService {

}
