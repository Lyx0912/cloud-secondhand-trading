package com.xhj.storage.controller;

import java.util.Arrays;
import java.util.Map;

import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.storage.entity.req.LogReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xhj.storage.entity.Log;
import com.xhj.storage.service.LogService;



/**
 * AT transaction mode undo table
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-09-10 17:08:18
 */
@RestController
@RequestMapping("/log")
public class LogController {
    @Autowired
    private LogService undoLogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(LogReq req){
        PageUtils page = undoLogService.queryPage(req);

        return R.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/{branchId}")
    public R info(@PathVariable("branchId") Long branchId){
		Log undoLog = undoLogService.getById(branchId);

        return R.ok(undoLog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody Log undoLog){
		undoLogService.save(undoLog);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody Log undoLog){
		undoLogService.updateById(undoLog);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] branchIds){
		undoLogService.removeByIds(Arrays.asList(branchIds));

        return R.ok();
    }

}
