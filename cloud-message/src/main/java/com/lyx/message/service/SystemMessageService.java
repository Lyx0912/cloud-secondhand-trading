package com.lyx.message.service;

import com.lyx.common.mp.utils.PageUtils;
import com.lyx.message.entity.SystemMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyx.message.entity.req.SystemMessageListPageReq;
import com.lyx.message.entity.vo.SystemMessageVO;

/**
 * <p>
 * 系统消息表 服务类
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-04-17 03:06:01
 */
public interface SystemMessageService extends IService<SystemMessage> {

     /**
       * 分页查询系统消息
       */
    PageUtils<SystemMessageVO> listPage(SystemMessageListPageReq req);
}
