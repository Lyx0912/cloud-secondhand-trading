package com.lyx.goods.controller;

import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Audit;
import com.lyx.goods.entity.req.AuditListPageReq;
import com.lyx.goods.entity.req.AuditSaveReq;
import com.lyx.goods.entity.req.GoodsListPageReq;
import com.lyx.goods.entity.req.GoodsSaveReq;
import com.lyx.goods.entity.vo.AuditVo;
import com.lyx.goods.entity.vo.GoodsVO;
import com.lyx.goods.service.AuditService;
import com.lyx.goods.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
     * 分页查询商品审核列表
     */
    @GetMapping("/list")
    public R list(AuditListPageReq req){
        PageUtils<AuditVo> pageUtils = auditService.listPage(req);
        return R.ok(pageUtils);
    }

    /**
     * 删除审核商品
     */
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable List<Long> ids){
        // 逻辑删除商品
        auditService.auditremoveByIds(ids);
        return R.ok();
    }

    /**
     * 更新商品审核状态
     */
    @PutMapping()
    public R update(@RequestBody List<AuditSaveReq> req) {
        auditService.updateAuditState(req);
        return R.ok();
    }


}
