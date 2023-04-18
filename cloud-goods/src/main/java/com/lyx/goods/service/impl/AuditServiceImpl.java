package com.lyx.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.goods.entity.Audit;
import com.lyx.goods.mapper.AuditMapper;
import com.lyx.goods.service.AuditService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author: xhj
 * @Date: 2023/04/17/14:43
 * @Description:
 */
@Service
public class AuditServiceImpl extends ServiceImpl<AuditMapper, Audit> implements AuditService {
}
