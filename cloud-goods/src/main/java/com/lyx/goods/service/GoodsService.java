package com.lyx.goods.service;

import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyx.goods.entity.req.GoodsListPageReq;
import com.lyx.goods.entity.req.GoodsSaveReq;
import com.lyx.goods.entity.vo.GoodsVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 黎勇炫
 * @since 2023-03-31 11:16:28
 */
public interface GoodsService extends IService<Goods> {

     /**
       * 分页查询商品
       */
    PageUtils<GoodsVO> listPage(GoodsListPageReq req);

     /**
       * 切换商品上架状态
       */
    void changeIsOnSell(List<Long> goodsIds, Integer isOnSell);

     /**
       * 查询商品详情
       */
    GoodsVO getGoodsVOById(Long id);

     /**
       * 更新商品详情
       */
    void updateGoodsInfo(GoodsSaveReq req);
}
