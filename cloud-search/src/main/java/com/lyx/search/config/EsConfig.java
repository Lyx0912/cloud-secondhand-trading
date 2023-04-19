package com.lyx.search.config;

import lombok.Setter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 黎勇炫
 * @date 2023年03月25日 13:39
 */
@Configuration
@ConfigurationProperties(prefix = "es")
public class EsConfig {


    @Setter
    private String host;
    @Setter
    private int port;
    @Setter
    private String type;

    public static final RequestOptions COMMON_OPTIONS;

    static {
        COMMON_OPTIONS = RequestOptions.DEFAULT.toBuilder().build();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost("47.120.6.55", 9200, "http"));
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        return restHighLevelClient;
    }

}
