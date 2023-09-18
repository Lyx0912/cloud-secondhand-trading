package com.xhj.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @Author: xhj
 * @Date: 2023/04/24/13:17
 * @Description:
 */
@SpringBootApplication
@EnableFeignClients
@EnableRedisHttpSession
public class OrderApp {


    public static void main(String[] args) {
        SpringApplication.run(OrderApp.class,args);
    }

}
