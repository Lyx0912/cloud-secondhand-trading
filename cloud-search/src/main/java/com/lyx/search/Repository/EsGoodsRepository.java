package com.lyx.search.Repository;

import com.lyx.search.entity.GoodsEs;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * @Author: xhj
 * @Date: 2023/09/13/13:16
 * @Description:
 */
@Component
public interface EsGoodsRepository extends ElasticsearchRepository<GoodsEs,String> {
}
