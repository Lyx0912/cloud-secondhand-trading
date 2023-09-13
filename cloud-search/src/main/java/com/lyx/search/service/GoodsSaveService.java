package com.lyx.search.service;

import com.lyx.common.base.entity.dto.GoodsDTO;
import com.lyx.common.base.entity.dto.GoodsEsDTO;

import java.io.IOException;
import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/04/19/14:47
 * @Description:
 */
public interface GoodsSaveService {

    void goodsStatusUp(List<GoodsEsDTO> goodsEsDTOS);

    void goodsDelete(List<Long> ids);

    List<GoodsDTO>  goodsEsList();
}
