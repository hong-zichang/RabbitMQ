package com.zichang.springboot_rabbitmq.controller;

/**
 * author: ZiChangHong
 * create-date: 2022/10/4 15:26
 **/

import com.zichang.springboot_rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * 消息发送控制层
 */
@Slf4j
@RestController
@RequestMapping("confirm")
public class ProducerController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("sendMessage/{message}")
    public void sendMsg(@PathVariable("message") String Msg){
        rabbitTemplate.convertAndSend(
                ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY,
                Msg,
                new CorrelationData(rabbitTemplate.getUUID()));
        log.info("发送消息内容：{}", Msg);

        rabbitTemplate.convertAndSend(
                ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY + "123",
                Msg,
                new CorrelationData(rabbitTemplate.getUUID()));
        log.info("发送消息内容：{}", Msg);
    }
}
