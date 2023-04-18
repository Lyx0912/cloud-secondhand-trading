package com.lyx.goods.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Audit;
import com.lyx.goods.entity.Goods;
import com.lyx.goods.entity.req.GoodsListPageReq;
import com.lyx.goods.entity.vo.AuditVo;
import com.lyx.goods.entity.vo.GoodsVO;
import com.lyx.goods.mapper.AuditMapper;
import com.lyx.goods.service.AuditService;
import com.lyx.goods.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
@Slf4j
public class AuditServiceImpl extends ServiceImpl<AuditMapper, Audit> implements AuditService {

    @Autowired
    GoodsService goodsService;

    /**
     *
     * @param req
     * @return
     */
    @Transactional
    @Override
    public PageUtils<AuditVo> listPage(GoodsListPageReq req) {
        Page<AuditVo> page = new Page<>(req.getPageNo(),req.getPageSize());
        // 分页查询商品列表
        PageUtils<GoodsVO> goodsVOPageUtils = goodsService.listPage(req);
        // 商品列表遍历
        List<AuditVo> auditVos = goodsVOPageUtils.getList().stream().map(good -> {
            AuditVo auditVo = new AuditVo();
            // 属性对拷
            BeanUtils.copyProperties(good, auditVo);
            // 查询对应状态信息
            Audit audit = this.getById(good.getId());
            auditVo.setState(audit.getState());
            auditVo.setMark(audit.getMark());
            System.out.println(auditVo);
            log.info("auditVo=={}", auditVo);
            return auditVo;
        }).collect(Collectors.toList());
        page.setRecords(auditVos);
        page.setTotal(auditVos.stream().count());
        return PageUtils.build(page);
    }
}
