package com.zichang.springboot_rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;

/**
 * author: ZiChangHong
 * create-date: 2022/10/4 15:39
 **/

/**
 * 发布确认的回调函数
 */
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        //注入回调函数
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * @param correlationData 保存回调消息的ID以及相关信息
     * @param ack             是否收到消息 也就是确认
     * @param cause           未收到消息的原因
     *                        1.交换机收到了消息 回调
     *                        1.1 correlationData 保存回调消息的ID以及相关信息
     *                        1.2 ack 是否收到消息 也就是确认 true
     *                        1.3 cause 未收到消息的原因 null
     *                        2.交换机没有收到消息
     *                        2.1 correlationData 保存回调消息的ID以及相关信息
     *                        2.2 ack 是否收到消息 也就是确认 false
     *                        2.3 cause 未收到消息的原因 String
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {

            log.info("交换机已经收到了id为{}的消息", correlationData.getId());

        } else {
            log.info("交换机未收到了id为{}的消息", correlationData.getId());
        }
    }

    //回退方法，不可达时返回给生产者 优先级比备份交换机低
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error("消息{}，被交换机{}退回，退回原因：{}，路由key：{}",
                new String(returned.getMessage().getBody()),
                returned.getExchange(),
                returned.getReplyText(),
                returned.getRoutingKey());
    }
}
