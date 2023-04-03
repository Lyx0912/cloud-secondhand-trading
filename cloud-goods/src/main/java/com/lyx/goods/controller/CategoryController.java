package com.lyx.goods.controller;


import com.lyx.common.base.result.R;
import com.lyx.goods.entity.Category;
import com.lyx.goods.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/list")
    public R list(){
        // todo 分类查询
        List<Category> res = categoryService.categoryTree();
        return R.ok(res);
    }
}

