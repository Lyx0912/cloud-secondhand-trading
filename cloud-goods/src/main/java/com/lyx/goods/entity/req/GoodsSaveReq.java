package com.lyx.goods.entity.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lyx.goods.entity.GoodsDetails;
import com.lyx.goods.entity.GoodsImages;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author 黎勇炫
 * @date 2023年04月10日 14:58
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoodsSaveReq {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 所属分类
     */
    @NotNull(message = "请选择所属分类")
    private Long cid;


    /**
     * 物品名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;


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
    private GoodsDetails details;

     /**
       * 商品图片
       */
    @NotNull(message = "请先上传图片集")
    private List<GoodsImages> images;

    /**
     * 展示图片
     */
    private String defaultImg;

     /**
       * 分类路径
       */
    private Long[] categoryPath;

    /**
     * 库存
     */
    private Integer total;

}
