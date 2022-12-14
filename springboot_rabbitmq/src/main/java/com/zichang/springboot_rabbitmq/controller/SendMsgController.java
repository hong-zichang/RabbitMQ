package com.zichang.springboot_rabbitmq.controller;

/**
 * author: ZiChangHong
 * create-date: 2022/10/3 17:20
 **/

import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 发送消息的控制层
 */
@RestController
@RequestMapping("ttl")
@Slf4j
public class SendMsgController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable("message") String msg){
        log.info("当前时间：{}，发送一条消息给两个TTL队列：{}", new Date().toString(), msg);
        rabbitTemplate.convertAndSend("X", "XA", "消息来着ttl为10s的队列:" + msg);
        rabbitTemplate.convertAndSend("X", "XB", "消息来着ttl为40s的队列:" + msg);
    }

    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    @ApiParam("可定制延迟时间的消息")
    public void sendExpirationMsg(@PathVariable("message") String msg,
                                  @PathVariable("ttlTime") Long ttl){
        log.info("当前时间：{}，发送一条消息给TTL队列：{},延时为：{}ms", new Date().toString(), msg, ttl);
        rabbitTemplate.convertAndSend("X", "XC", "消息来着ttl为" + ttl + "ms的队列:" + msg,
                correlationData ->{
                    correlationData.getMessageProperties().setExpiration(String.valueOf(ttl));
                    return correlationData;
                });
    }

    //基于插件的延迟队列
    @GetMapping("sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable("message") String msg,
                        @PathVariable("delayTime") Integer delayTime){
        log.info("当前时间：{}，发送一条消息给基于插件的延迟消息队列：{},延时为：{}ms", new Date().toString(), msg, delayTime);
        rabbitTemplate.convertAndSend("delayed.exchange", "delayed.routingkey", "消息来着基于延迟队列插件延迟时间为" + delayTime + "ms的队列:" + msg,
                correlationData ->{
                    correlationData.getMessageProperties().setDelay(delayTime);
                    return correlationData;
                });
    }
}
