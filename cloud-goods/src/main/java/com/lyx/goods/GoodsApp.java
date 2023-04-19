package com.lyx.goods;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Collections;

/**
 * @author 黎勇炫
 * @date 2023年03月23日 16:14
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients("com.lyx.goods.feign")
public class GoodsApp {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApp.class,args);
    }
}
