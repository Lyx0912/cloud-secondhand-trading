package com.lyx.search;

import com.alibaba.fastjson.JSON;
import com.lyx.search.config.EsConfig;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

/**
 * @author 黎勇炫
 * @date 2023年03月25日 13:58
 */
@SpringBootApplication
public class SearchApp {

//    static class Users{
//        public String userName;
//        public Integer age;
//
//        public Users(String userName, Integer age) {
//            this.userName = userName;
//            this.age = age;
//        }
//    }

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext run = SpringApplication.run(SearchApp.class, args);
//        RestHighLevelClient restHighLevelClient = (RestHighLevelClient) run.getBean("restHighLevelClient");
//        IndexRequest indexRequest  = new IndexRequest("users");
//        indexRequest.id("1");
//
//        Users user = new Users("黎勇炫", 18);
//
//        indexRequest.source(JSON.toJSONString(user), XContentType.JSON);
//
//        IndexResponse indexResponse = restHighLevelClient.index(indexRequest,EsConfig.COMMON_OPTIONS);
//        System.out.println(indexResponse);
    }
}
