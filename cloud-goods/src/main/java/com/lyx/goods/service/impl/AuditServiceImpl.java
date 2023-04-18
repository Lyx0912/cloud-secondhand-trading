package com.lyx.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Audit;
import com.lyx.goods.entity.Goods;
import com.lyx.goods.entity.req.AuditListPageReq;
import com.lyx.goods.entity.req.GoodsListPageReq;
import com.lyx.goods.entity.vo.AuditVo;
import com.lyx.goods.entity.vo.GoodsVO;
import com.lyx.goods.mapper.AuditMapper;
import com.lyx.goods.service.AuditService;
import com.lyx.goods.service.CategoryService;
import com.lyx.goods.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    private GoodsService goodsService;

    @Autowired
    private CategoryService categoryService;

    /**
     *
     * @param req
     * @return
     */
    @Transactional
    @Override
    public PageUtils<AuditVo> listPage(AuditListPageReq req) {
        Page<AuditVo> page = new Page<>(req.getPageNo(),req.getPageSize());
        // 查询商品列表
        LambdaQueryWrapper<Goods> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotEmpty(req.getName()),Goods::getName,req.getName())
                .like(StringUtils.isNotEmpty(req.getSeller()),Goods::getSeller,req.getSeller());
        List<Goods> goods = goodsService.list(wrapper);
        // 商品列表遍历
        List<AuditVo> auditVos = goods.stream().map(good -> {
            AuditVo auditVo = new AuditVo();
            // 属性对拷
            BeanUtils.copyProperties(good, auditVo);
            // 查询对应状态信息
            Audit audit = this.getById(good.getId());
            auditVo.setState(audit.getState());
            auditVo.setMark(audit.getMark());
            // 查询对应分类名称
            auditVo.setCategoryName(categoryService.getById(good.getCid()).getName());
            return auditVo;
        }).collect(Collectors.toList());
        if (req.getState()!=null){
            auditVos = auditVos.stream().filter(auditVo -> auditVo.getState() == req.getState()).collect(Collectors.toList());
        }
        page.setRecords(auditVos);
        page.setTotal(auditVos.stream().count());
        return PageUtils.build(page);
    }
}
