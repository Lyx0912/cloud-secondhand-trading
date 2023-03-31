package com.lyx.goods.service.impl;

import com.lyx.goods.entity.Category;
import com.lyx.goods.mapper.CategoryMapper;
import com.lyx.goods.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-31 11:16:28
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    /**
     * 构建商品分类(树形结构)
     */
    @Override
    public List<Category> categoryTree() {
        // 查询所有的分类
        List<Category> allRes = list();
        // 遍历分类列表 构建树形结构

        // 一级分类
        List<Category> parent = allRes.stream().filter(item -> {
            return item.getCatLevel() == 0;
        }).collect(Collectors.toList());

        // 递归查找子分类
        buildParentCategory(parent,allRes);
        return null;
    }

     /**
       * 查找所属子分类
       */
    private void buildParentCategory(List<Category> parent, List<Category> allRes) {
        // todo
    }
}
