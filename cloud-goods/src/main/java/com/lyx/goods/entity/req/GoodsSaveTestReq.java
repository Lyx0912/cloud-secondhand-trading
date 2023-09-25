package com.lyx.goods.entity.req;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.lyx.goods.entity.GoodsDetails;
import com.lyx.goods.entity.GoodsImages;
import com.lyx.goods.entity.vo.GoodsImagesVo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/09/12/15:12
 * @Description:
 */
@Data
public class GoodsSaveTestReq {

    /**
     * 商品id
     */
    private Long id;
    /**
     * 物品名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 卖家名称
     */
    @NotBlank(message = "名称不能为空")
    private String seller;

    /**
     * 卖家
     */
    @ExcelIgnore
//    @NotBlank(message = "sellerId不能为空")
    private Integer sellerId;


    /**
     * 描述
     */
    @NotBlank(message = "描述信息不能为空")
    private String description;

    /**
     * 价格
     */
    @NotNull(message = "请输入商品价格")
    private BigDecimal price;


    /**
     * 是否上架
     */
    private Integer isOnSell;

    /**
     * 商品详情
     */
//    @NotNull (message = "商品详情不能为空")
    private GoodsDetails details;

    /**
     * 商品图片
     */
    @NotNull(message = "请先上传图片")
    private List<GoodsImagesVo> images;

    /**
     * 展示图片
     */
    @NotNull(message = "请先上传图片集")
    private String defaultImg;

    /**
     * 分类路径
     */
    @NotNull(message = "分类不能为空")
    private Long[] categoryPath;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    private Integer total;
}
