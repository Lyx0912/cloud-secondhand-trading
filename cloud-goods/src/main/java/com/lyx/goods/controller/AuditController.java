package com.lyx.goods.controller;

import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.req.GoodsListPageReq;
import com.lyx.goods.entity.vo.AuditVo;
import com.lyx.goods.entity.vo.GoodsVO;
import com.lyx.goods.service.AuditService;
import com.lyx.goods.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xhj
 * @Date: 2023/04/17/14:35
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/audit")
public class AuditController {

    @Autowired
    AuditService auditService;

    @Autowired
    GoodsService goodsService;

    /**
     * 分页查询商品列表
     */
    @GetMapping("/list")
    public R list(GoodsListPageReq req){
        PageUtils<AuditVo> pageUtils = auditService.listPage(req);
        return R.ok(pageUtils);
    }




}
