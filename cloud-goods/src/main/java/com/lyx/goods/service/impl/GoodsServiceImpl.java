package com.lyx.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.common.base.entity.PageUtils;
import com.lyx.goods.entity.Goods;
import com.lyx.goods.entity.req.GoodsListPageReq;
import com.lyx.goods.entity.vo.GoodsVO;
import com.lyx.goods.mapper.GoodsMapper;
import com.lyx.goods.service.GoodsService;
import org.apache.ibatis.executor.statement.StatementUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-31 11:16:28
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    /**
     * 分页查询商品
     * todo 未完成代码逻辑
     * @param req 查询条件对象
     */
    @Override
    public PageUtils<GoodsVO> listPage(GoodsListPageReq req) {
        // 构建分页对象 设置分页参数
        Page<GoodsVO> page = new Page<>(req.getPageNo(), req.getPageSize());
        // 构建查询条件
        LambdaQueryWrapper<GoodsVO> wrapper = Wrappers.lambdaQuery();

        baseMapper.listPage(page);
        PageUtils<GoodsVO> pageUtils = new PageUtils<>();
        // 转换vo
//        pageUtils.setList(memberMapStruct.memberToMemberVO(page.getRecords()));
        pageUtils.setPageNo(page.getCurrent());
        pageUtils.setTotal(page.getTotal());
        // 返回分页对象
        return pageUtils;
    }
}
