package com.lyx.goods.service;

import com.lyx.common.base.entity.dto.GoodsDTO;
import com.lyx.common.base.entity.dto.OrderGoodsDTO;
import com.lyx.common.base.entity.dto.OrderGoodsPageReqDTO;
import com.lyx.common.mp.utils.PageUtils;
import com.lyx.goods.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyx.goods.entity.req.GoodsListPageReq;
import com.lyx.goods.entity.req.GoodsSaveReq;
import com.lyx.goods.entity.req.GoodsSaveTestReq;
import com.lyx.goods.entity.vo.GoodsReleaseVo;
import com.lyx.goods.entity.vo.GoodsVO;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
    GoodsVO getGoodsVOById(Long id) throws ExecutionException, InterruptedException;
     /**
       * 远程查询商品详情
       */
    GoodsVO getGoodsVOFeignById(Long id);


    /**
     * 查询商品详情
     */
    GoodsDTO getGoodsDTOById(Long id) throws ExecutionException, InterruptedException;

     /**
       * 更新商品详情
       */
    void updateGoodsInfo(GoodsSaveReq req);

    /**
     * 发布商品
     * @param req
     */
    void saveGoodsInfo(GoodsSaveTestReq req);

    /**
     * 查询已上传商品
     * @param req
     * @return
     */
    PageUtils<GoodsReleaseVo> releaseGoodslistPage(GoodsListPageReq req);
    /**
     * 用户分页查询商品列表
     */
    PageUtils<GoodsVO> listEsPage(GoodsListPageReq req);

    /**
     * 删除商品
     * @param ids
     */
    void removeGoodsByIds(List<Long> ids);

    /**
     * 查询用户发布商品数量
     * @param memberId
     * @return
     */
    Integer memberIdCount(Long memberId);
    /**
     * 用户查询商品列表
     */
    List<GoodsDTO> goodsList(OrderGoodsPageReqDTO reqDTO);
    /**
     * 查询商品详情
     */
    OrderGoodsDTO getorderGoodsInfo(Long id);

    /**
     * 重新发布
     * @param req
     */
    void updateRes(GoodsSaveTestReq req);
    /**
     * 查询商品所有消息
     */
//    void getGoods(GoodsSaveTestReq req);
    /**
     * 查询用户发布商品id
     */
    List<Long> getGoodsById(Long memberId);

    List<GoodsVO> listIds(List<Long> ids);
}
