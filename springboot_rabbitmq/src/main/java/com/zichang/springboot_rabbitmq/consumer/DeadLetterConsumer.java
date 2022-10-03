package com.zichang.springboot_rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * author: ZiChangHong
 * create-date: 2022/10/3 17:27
 **/
@Component
@Slf4j
public class DeadLetterConsumer {

    //接收消息
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) throws UnsupportedEncodingException {
        String msg = new String(message.getBody(), "UTF-8");
        log.info("当前时间：{}，收到死信队列的消息：{}", new Date().toString(), msg);
    }
}
