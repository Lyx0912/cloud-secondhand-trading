package com.lyx.goods.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lyx.common.base.result.R;
import com.lyx.common.web.utils.ResponseUtils;
import com.lyx.goods.entity.Category;
import com.lyx.goods.entity.req.CategorySaveReq;
import com.lyx.goods.entity.vo.CategoryVo;
import com.lyx.goods.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import utils.ExcelUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-31 11:16:28
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

     /**
       * 查询分类菜单树
       */
    @GetMapping("/list")
    public R list(){
        List<Category> res = categoryService.categoryTree();
        return R.ok(res);
    }
     /**
       * 查询1号分类菜单
       */
    @GetMapping()
    public R category(){
        List<CategoryVo> res = categoryService.category();
        return R.ok(res);
    }

     /**
       * 删除一个分类(包括子分类)
       */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id){
        categoryService.removeCategory(id);
        return R.ok();
    }

    /**
     * 添加分类
     */
    @PutMapping()
    public R save(@Validated @RequestBody CategorySaveReq req){
        categoryService.saveCategory(req);
        return R.ok();
    }
    
     /**
       * 批量删除分类
       */
    @DeleteMapping("/batchDelete/{ids}")
    public R batchDelete(@PathVariable List<Long> ids){
        categoryService.batchDelete(ids);
        return R.ok();
    }

     /**
       * 导出分类列表
       */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        // 查询所有的分类
        List<Category> categories = categoryService.list();
        ExcelUtils.export(ResponseUtils.excelResponse(response).getOutputStream(),Category.class,categories,"分类列表");
    }

}

