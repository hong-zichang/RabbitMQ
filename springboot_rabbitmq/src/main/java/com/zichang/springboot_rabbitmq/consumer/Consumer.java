package com.zichang.springboot_rabbitmq.consumer;

/**
 * author: ZiChangHong
 * create-date: 2022/10/4 15:30
 **/

import com.zichang.springboot_rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 接收消息
 */
@Component
@Slf4j
public class Consumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMessage(Message message) throws Exception {
        String msg = new String(message.getBody(), "UTF-8");
        log.info("接收到队列{}的消息：{}", ConfirmConfig.CONFIRM_QUEUE_NAME, msg);
    }
}
