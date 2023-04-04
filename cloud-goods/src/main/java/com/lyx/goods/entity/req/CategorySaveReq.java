package com.lyx.goods.entity.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 黎勇炫
 * @date 2023年04月04日 13:12
 */
@Data
public class CategorySaveReq {

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    private String name;

    /**
     * 父分类id
     */
    @NotNull(message = "父分类编号不能为空")
    private Long parentCid;

    /**
     * 层级
     */
    @NotNull(message = "层级不能为空")
    private Integer catLevel;

    /**
     * 是否显示[0-不显示，1显示]
     */
    private Integer showStatus = 1;

    /**
     * 排序（默认999）
     */
    private Integer sort = 999;

    /**
     * 图标地址
     */
    private String icon;

    /**
     * 计量单位
     */
    private String productUnit;

    /**
     * 商品数量
     */
    private Integer productCount = 0;
}
