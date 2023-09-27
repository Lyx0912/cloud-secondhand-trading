package com.xhj.order.listener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.xhj.order.entity.req.OrderPaymentReq;
import com.xhj.order.entity.vo.OrderAddrVo;
import com.xhj.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.retry.MessageKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author: xhj
 * @Date: 2023/09/18/13:52
 * @Description:
 */
@Service
@RabbitListener(queues = "secondhandOrder.release.queue")
@Slf4j
public class OrderListener {

    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void OrderHandler(OrderAddrVo orderAddrVo,Channel channel, Message message) throws IOException {
        try{
            OrderPaymentReq req = new OrderPaymentReq();
            req.setOrderSn(Long.parseLong(orderAddrVo.getId()));
            req.setGoodsId(orderAddrVo.getGoodsId());
            req.setMemberId(orderAddrVo.getMemberAddrDTO().getMemberId());
            orderService.rabbitMqDeleteOrderByOrderId(req);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            log.info("rabbitmq 订单超时取消{}",orderAddrVo);
        }catch (Exception e){
            // 重新入队
//            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
            //重新发送消息到队尾
//            channel.basicPublish(message.getMessageProperties().getReceivedExchange(),
//                    message.getMessageProperties().getReceivedRoutingKey(),
//                    MessageProperties.PERSISTENT_TEXT_PLAIN,
//                    orderAddrVo.toString().getBytes(StandardCharsets.UTF_8));
        }
    }
}
