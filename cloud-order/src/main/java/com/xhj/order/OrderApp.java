package com.xhj.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: xhj
 * @Date: 2023/04/24/13:17
 * @Description:
 */
@SpringBootApplication
@EnableFeignClients
public class OrderApp {

    public static void main(String[] args) {
        SpringApplication.run(OrderApp.class,args);
    }

}
