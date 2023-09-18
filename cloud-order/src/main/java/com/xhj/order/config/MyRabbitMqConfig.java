package com.xhj.order.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Author: xhj
 * @Date: 2023/09/18/11:57
 * @Description:
 */
@Configuration
public class MyRabbitMqConfig {
    /**
     * 设置rabbit序列化
     * @return
     */
    @Bean
    public MessageConverter messageContext(){
        return new Jackson2JsonMessageConverter();
    }
}
