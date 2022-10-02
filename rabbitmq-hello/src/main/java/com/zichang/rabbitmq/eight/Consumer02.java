package com.zichang.rabbitmq.eight;

/**
 * author: ZiChangHong
 * create-date: 2022/10/2 21:44
 **/

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zichang.rabbitmq.utils.RabbitMQUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 死信实战 消费者C2
 */
public class Consumer02 {
    //死信交换机名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //接收消息
        //接收消息的回调函数
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer02接收到消息：" + new String(message.getBody(), "UTF-8"));
        };
        //取消消息的回调函数
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费者取消接收消息回调函数触发。。。。" + consumerTag);
        };
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, cancelCallback);
    }
}
