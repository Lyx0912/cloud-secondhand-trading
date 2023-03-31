package com.lyx.goods.mapper;

import com.lyx.goods.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyx.goods.service.CategoryService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

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


}
