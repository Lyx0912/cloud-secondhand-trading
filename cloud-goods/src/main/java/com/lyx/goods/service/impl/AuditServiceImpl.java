package com.lyx.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.lyx.common.base.entity.dto.GoodsEsDTO;
import com.lyx.common.base.exception.BizException;
import com.lyx.common.base.result.ResultCode;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Audit;
import com.lyx.goods.entity.Goods;
import com.lyx.goods.entity.req.AuditListPageReq;
import com.lyx.goods.entity.req.AuditSaveReq;
import com.lyx.goods.entity.vo.AuditVo;
import com.lyx.goods.entity.vo.GoodsVO;
import com.lyx.goods.feign.SearchElasticFeignService;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private SearchElasticFeignService feignService;

    /**
     *  分页查询商品审核列表
     * @param req
     * @return
     */
    @Transactional
    @Override
    public PageUtils<AuditVo> listPage(AuditListPageReq req) {
        Page<AuditVo> page = new Page<>(req.getPageNo(),req.getPageSize());
        // 创建查询商品审核条件
        LambdaQueryWrapper<Audit> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(req.getState()!=null,Audit::getState,req.getState());
        // 查询商品审核列表
        List<Audit> audits = this.list(wrapper);
        List<AuditVo> auditVos = audits.stream().map(audit -> {
            // 创建Goods 查询条件
            LambdaQueryWrapper<Goods> lambdaQuery = Wrappers.lambdaQuery();
            lambdaQuery.like(StringUtils.isNotEmpty(req.getName()),Goods::getName,req.getName())
                            .like(StringUtils.isNotEmpty(req.getSeller()),Goods::getSeller,req.getSeller())
                                    .eq(Goods::getId,audit.getGoodsId());
            // 查询 商品 信息
            Goods goods = goodsService.getOne(lambdaQuery);
//            Goods goods = goodsService.getById(audit.getGoodsId());
            // 如果 goods 不为空
            if (goods != null) {
                AuditVo auditVo = new AuditVo();
                // 属性对拷
                BeanUtils.copyProperties(goods, auditVo);
                auditVo.setState(audit.getState());
                auditVo.setMark(audit.getMark());
                // 查询对应分类名称
                auditVo.setCategoryName(categoryService.getById(goods.getCid()).getName());
                return auditVo;
            }
            // goods 为空
            return null;
            //过滤掉空值
        }).filter(auditVo -> auditVo!=null).collect(Collectors.toList());
        page.setRecords(auditVos);
        page.setTotal(auditVos.stream().count());
        return PageUtils.build(page);
    }

    /**
     * 商品审核
     * @param reqs
     */
    @Transactional
    @Override
    public void updateAuditState(List<AuditSaveReq> reqs) {

        List<GoodsEsDTO> goodsEsDTOS = reqs.stream().map(req -> {
            // 判断是否已经上架
            Integer count = lambdaQuery().in(Audit::getState, 1)
                    .eq(Audit::getGoodsId, req.getGoodsId()).count();
            if (count > 0) {
                throw new BizException(ResultCode.AUDIT_ALREADY_ON_THE_SHELVES);
            }
            // 构建 Audit 更新条件 修改审核状态码
            LambdaUpdateWrapper<Audit> wrapper = Wrappers.lambdaUpdate();
            wrapper.set(Audit::getState, req.getState())
                    .set(StringUtils.isNotEmpty(req.getMark()), Audit::getMark, req.getMark())
                    .eq(Audit::getGoodsId, req.getGoodsId());
            this.update(wrapper);
            // 构建 Goods 更新条件 修改上架状态码
            LambdaUpdateWrapper<Goods> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.set(Goods::getIsOnSell, req.getState() == 1 ? 1 : 0)
                    .eq(Goods::getId, req.getGoodsId());
            goodsService.update(updateWrapper);
            // 获取 id 对应的 goodsVO信息
            Goods goods = goodsService.getById(req.getGoodsId());
            return goods;
        }).map(goods -> {
            GoodsEsDTO goodsEsDTO = new GoodsEsDTO();
            BeanUtils.copyProperties(goods, goodsEsDTO);
            return goodsEsDTO;
        }).collect(Collectors.toList());
        log.info("goodsEsDTOS",goodsEsDTOS);
        // 远程调用 商品上架到 Elasticsave
        feignService.goodsStatusUp(goodsEsDTOS);
    }

    /**
     * 删除商品审核列表
     * @param ids
     */
    @Transactional
    @Override
    public void auditremoveByIds(List<Long> ids) {
        this.removeByIds(ids);
    }

    /**
     * 查询 商品审核状态
     * @param id
     * @return
     */
    @Override
    public Long getAuditById(Long id) {
        return baseMapper.getAuditById(id);
    }

}
