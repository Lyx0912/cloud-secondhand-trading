package com.lyx.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Audit;
import com.lyx.goods.entity.req.AuditListPageReq;
import com.lyx.goods.entity.req.AuditSaveReq;
import com.lyx.goods.entity.req.GoodsListPageReq;
import com.lyx.goods.entity.vo.AuditVo;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @Author: xhj
 * @Date: 2023/04/17/14:43
 * @Description:
 */
public interface AuditService extends IService<Audit> {

    PageUtils<AuditVo> listPage(AuditListPageReq req);

    void updateAuditState(List<AuditSaveReq> req);

    void auditremoveByIds(List<Long> ids);

    Long getAuditById(Long id);
}
