package com.zichang.rabbitmq.eight;

/**
 * author: ZiChangHong
 * create-date: 2022/10/2 22:09
 **/

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.zichang.rabbitmq.utils.RabbitMQUtils;

import java.nio.charset.StandardCharsets;

/**
 * 死信队列生产者代码
 */
public class Producer {
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        //死信消息 设置TTL时间 过期时间为10s 10000ms
        AMQP.BasicProperties prop = new AMQP.BasicProperties()
                .builder().expiration("10000").build();
        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", prop, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
