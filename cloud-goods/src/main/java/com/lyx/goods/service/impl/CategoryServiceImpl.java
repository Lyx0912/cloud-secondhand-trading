package com.lyx.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lyx.common.base.constant.GlobalConstants;
import com.lyx.common.base.exception.BizException;
import com.lyx.common.base.result.ResultCode;
import com.lyx.goods.entity.Category;
import com.lyx.goods.entity.req.CategorySaveReq;
import com.lyx.goods.entity.vo.CategoryVo;
import com.lyx.goods.mapper.CategoryMapper;
import com.lyx.goods.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    @Override
    @Cacheable(value={"category"}, key="#root.method.name", sync = true)
    public List<Category> categoryTree() {
        List<Category> allRes = getCategoryList();
        // 遍历分类列表 构建树形结构
        List<Category> parents = allRes.stream().filter(item -> item.getParentCid() == 0).map(item->{
            item.setChildrens(buildParentCategory(item.getId(),allRes));
            return item;
        }).collect(Collectors.toList());
        // 构建树形结构
        return parents;
    }

    @Cacheable(value={"category"}, key="#root.method.name", sync = true)
    public List<Category> getCategoryList() {
        // 查询所有的分类
        List<Category> allRes = lambdaQuery().eq(Category::getShowStatus, GlobalConstants.STATUS_ON).orderByAsc(Category::getSort).list();
        return allRes;
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
        if(CollectionUtils.isEmpty(childrenList)) return null;
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
       * 查找父分类
       */
    @Override
    public Long[] findParentCategory(Long cid){
        List<Long> paths = new ArrayList<>();
        // 找到对应的实体
        Category category = baseMapper.selectById(cid);
        //找到所有父节点
        paths = this.doFindCateParent(paths, category);
        return paths.toArray(new Long[3]);
    }

     /**
       * 递归查找父分类
       */
    private List<Long> doFindCateParent(List<Long> paths, Category category) {
        // 如果是一级分类就直接添加并返回
        if(category.getCatLevel() == 1){
            paths.add(category.getId());
            return paths;
        }else {
            // 如果不是就继续找父分类
            Category parent = this.getById(category.getParentCid());
            paths = doFindCateParent(paths,parent);
            paths.add(category.getId());
            return paths;
        }
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

    /**
     * 查询1号分类菜单
     */
    @Override
    public List<CategoryVo> category() {
        LambdaQueryWrapper<Category> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Category::getCatLevel,1);
        List<Category> categories = list(wrapper);
        List<CategoryVo> categoryVos = categories.stream().map(category -> {
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category, categoryVo);
            return categoryVo;
        }).collect(Collectors.toList());

        return categoryVos;
    }
}
