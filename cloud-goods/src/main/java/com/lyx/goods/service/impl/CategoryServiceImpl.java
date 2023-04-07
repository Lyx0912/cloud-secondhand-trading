package com.lyx.goods.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lyx.common.base.constant.GlobalConstants;
import com.lyx.common.base.exception.BizException;
import com.lyx.common.base.result.ResultCode;
import com.lyx.goods.entity.Category;
import com.lyx.goods.entity.req.CategorySaveReq;
import com.lyx.goods.mapper.CategoryMapper;
import com.lyx.goods.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
     * todo 分布式场景下会有并发问题，需要引入分布式锁
     */
    @Cacheable(value={"category"}, key="#root.method.name", sync = true)
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

     /**
       * 逻辑删除分类 同时删除缓存中的数据
       */
    @Override
    @CacheEvict(value = "category",allEntries = true)
    public void removeCategory(Long id) {
        // 先查看是否有子分类
        Integer count = lambdaQuery().eq(Category::getParentCid, id).count();
        // 如果还有子分类就无法删除，需要先删除子分类
        if( count > 0 ){
            throw new BizException(ResultCode.CATEGORY_HAS_CHILD);
        }
        removeById(id);
    }

    /**
     * 添加分类
     *
     * @param req
     */
    @Override
    @CacheEvict(value = "category",allEntries = true)
    public void saveCategory(CategorySaveReq req) {
        Category category = new Category();
        BeanUtils.copyProperties(req,category);
        save(category);
    }

     /**
       * 递归查找负父分类
       */
    @Override
    public Category findParentCategory(Category category, List<Category> allCategory){
        for (Category item : allCategory) {
            if(item.getId().equals(category.getParentCid())){
                List<Category> childrens = new ArrayList<>();
                childrens.add(category);
                item.setChildrens(childrens);
                return findParentCategory(item,allCategory);
            }
        }
        return null;
    }

    @Override
    public Category findBelongCategory(Long cid) {
        List<Category> categories = this.categoryTree();
        // todo 找到叶子节点(找到所有二级节点，遍历他们的childrens)

        return null;
    }


    /**
     * 批量删除
     * @param ids
     */
    @Override
    @CacheEvict(value = "category",allEntries = true)
    public void batchDelete(List<Long> ids) {
        removeByIds(ids);
    }
}
