package com.lyx.goods.service;

import com.lyx.goods.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-31 11:16:28
 */
public interface CategoryService extends IService<Category> {

     /**
       * 构建商品分类(树形结构)
       */
    List<Category> categoryTree();

    void removeCategory(Long id);
}
