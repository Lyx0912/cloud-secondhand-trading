package com.lyx.search.service;

import com.lyx.search.entity.vo.GoodsEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/04/19/14:47
 * @Description:
 */
public interface GoodsSaveService {
    void goodsStatusUp(List<GoodsEsModel> goodsEsModels) throws IOException;
}
