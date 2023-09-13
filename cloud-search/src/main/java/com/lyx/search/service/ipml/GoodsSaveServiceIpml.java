package com.lyx.search.service.ipml;

import com.alibaba.fastjson.JSON;

import com.lyx.common.base.entity.dto.GoodsDTO;
import com.lyx.common.base.entity.dto.GoodsEsDTO;
import com.lyx.common.base.exception.BizException;
import com.lyx.common.base.result.ResultCode;
import com.lyx.search.Repository.EsGoodsRepository;
import com.lyx.search.config.EsConfig;
import com.lyx.search.entity.GoodsEs;
import com.lyx.search.service.GoodsSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: xhj
 * @Date: 2023/04/19/14:48
 * @Description:
 */
@Service
@Slf4j
public class GoodsSaveServiceIpml implements GoodsSaveService {

    @Autowired
    private RestHighLevelClient highLevelClient;
    @Autowired
    private EsGoodsRepository esGoodsRepository;

    /**
     * 商品查询
     */
    @Override
    public List<GoodsDTO>  goodsEsList(){
        Iterable<GoodsEs> goodsEs = esGoodsRepository.findAll();
        List<GoodsDTO> goodsEsList = new ArrayList<>();
        goodsEs.forEach(goods->{
            GoodsDTO goodsDTO = new GoodsDTO();
            BeanUtils.copyProperties(goods,goodsDTO);
            goodsEsList.add(goodsDTO);
        });
        return goodsEsList;
    }

    /**
     * 商品上架
     * @param goodsEsDTOS
     */
    @Override
    public void goodsStatusUp(List<GoodsEsDTO> goodsEsDTOS) {
        BulkRequest bulkRequest = new BulkRequest();
        for (GoodsEsDTO goodsEsDTO : goodsEsDTOS) {
            IndexRequest indexRequest = new IndexRequest("goods");
            indexRequest.id(goodsEsDTO.getId().toString());
            String s = JSON.toJSONString(goodsEsDTO);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }

        BulkResponse bulk = null;
        try {
            bulk = highLevelClient.bulk(bulkRequest, EsConfig.COMMON_OPTIONS);
        } catch (IOException e) {
            throw new BizException(ResultCode.AUDIT_THE_CONNECTION_IS_LOST);
        }

        boolean hasFailures = bulk.hasFailures();
        if (hasFailures){
            log.error("商品上架失败{}",hasFailures);
        }
        List<Object> collect = Arrays.asList(bulk.getItems()).stream().map(item -> {
            return item.getResponse();
        }).collect(Collectors.toList());

        log.info("商品上架完成：{}",collect);
    }

    /**
     * 商品下架
     * @param ids
     */
    @Override
    public void goodsDelete(List<Long> ids) {
        BulkRequest bulkRequest = new BulkRequest();
        for (Long id : ids) {
            DeleteRequest deleteRequest = new DeleteRequest("goods",id.toString());
            bulkRequest.add(deleteRequest);
        }
        try {
            highLevelClient.bulk(bulkRequest,EsConfig.COMMON_OPTIONS);
            log.info("下架成功！！！{}",ids);
        } catch (IOException e) {
            new BizException(ResultCode.AUDIT_THE_CONNECTION_IS_LOST);
        }
    }
}
