package com.lyx.goods.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyx.goods.entity.Goods;
import com.lyx.goods.entity.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-31 11:16:28
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    Page<GoodsVO> listPage(Page<GoodsVO> page,@Param("ew")  Wrapper<Goods> wrapper);

    GoodsVO getGoodsVOById(Long id);
}
