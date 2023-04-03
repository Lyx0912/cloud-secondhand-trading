package com.lyx.goods.service.impl;

import com.lyx.common.base.constant.GlobalConstants;
import com.lyx.goods.entity.Category;
import com.lyx.goods.mapper.CategoryMapper;
import com.lyx.goods.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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
     * todo 可以把这些数据存到redis
     */
    @Override
    public List<Category> categoryTree() {
        // 查询所有的分类
        List<Category> allRes = lambdaQuery().eq(Category::getShowStatus, GlobalConstants.STATUS_ON).orderByAsc(Category::getSort).list();
        // 遍历分类列表 构建树形结构
        List<Category> parents = allRes.stream().filter(item -> item.getParentCid() == 0).map(item->{
            item.setChildrens(buildParentCategory(item.getId(),allRes));
            return item;
        }).collect(Collectors.toList());
        // 构建树形结构
        return parents;
    }

     /**
       * 查找所属子分类
       */
    private List<Category> buildParentCategory(Long parentId, List<Category> allCat) {
        List<Category> childrenList = new ArrayList<>();
        // 查找父菜单为parentId的菜单
        allCat.stream().forEach(item->{
            if(item.getParentCid().equals(parentId)){
                // 添加到子菜单列表
                childrenList.add(item);
                // 递归查找
                item.setChildrens(buildParentCategory(item.getId(),allCat));
            };
        });

        //排序
        childrenList.sort(Comparator.comparingInt(item-> item.getSort()==null?0:item.getSort()));
        return childrenList;
    }
}
