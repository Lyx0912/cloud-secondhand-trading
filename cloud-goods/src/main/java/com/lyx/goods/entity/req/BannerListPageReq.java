package com.lyx.goods.entity.req;

import com.lyx.common.base.entity.PageReq;
import lombok.Data;

/**
 * @author 黎勇炫
 * @date 2023年04月13日 8:56
 */
@Data
public class BannerListPageReq extends PageReq {

    private String title;
    private Integer isActivite;
    private String description;
}
