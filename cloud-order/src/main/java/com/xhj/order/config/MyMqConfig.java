package com.xhj.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;


/**
 * @Author: xhj
 * @Date: 2023/09/18/12:07
 * @Description:
 */
@Configuration
public class MyMqConfig {

    /* 容器中的Queue、Exchange、Binding 会自动创建（在RabbitMQ）不存在的情况下 */

    /**
     * 普通队列
     */
    @Bean
    public Queue secondhandOrderReleaseQueue(){
        Queue queue = new Queue("secondhandOrder.release.queue",true,false,false);
        return queue;
    }

    /**
     * 主题交换机
     */
    @Bean
    public Exchange secondhandOrderEventExchange(){
        return new TopicExchange("secondhandOrder-event-exchange",true,false);
    }

    @Bean
    public Binding secondHandOrderCreateBinding(){
        return new Binding("secondhandOrder.delay.queue",
                Binding.DestinationType.QUEUE,
                "secondhandOrder-event-exchange",
                "secondhandOrder-create-order",null);
    }

    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue secondhandOrderDelayQueue(){
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "secondhandOrder-event-exchange");
        arguments.put("x-dead-letter-routing-key", "secondhandOrder-release-order");
        arguments.put("x-message-ttl", 6000); // 消息过期时间 1分钟
        Queue queue = new Queue("secondhandOrder.delay.queue",true,false,false,arguments);
        return queue;
    }

    @Bean
    public Binding secondHandOrderReleaseBinding(){
        return new Binding("secondhandOrder.release.queue",
                Binding.DestinationType.QUEUE,
                "secondhandOrder-event-exchange",
                "secondhandOrder-release-order",null);
    }

}
