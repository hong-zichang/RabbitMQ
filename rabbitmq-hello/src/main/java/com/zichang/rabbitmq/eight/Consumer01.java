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
import java.util.concurrent.TimeUnit;

/**
 * 死信实战 消费者C1
 * 3种情况会被拒绝
 * 1.消息过期，TTL
 * 2.超过队列最大长度
 * 3.消息被拒绝
 */
public class Consumer01 {
    //正常交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //正常队列名称
    public static final String NORMAL_QUEUE = "normal_queue";
    //死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明正常交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //声明死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //声明正常队列
        Map<String, Object> arguments = new HashMap<>();
        //设置正常消息队列的过期时间 ttl Time To Live
        //arguments.put("x-message-ttl", 10000);
        //设置正常队列的死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //设置正常队列的信息routing_key
        arguments.put("x-dead-letter-routing-key", "lisi");
        //设置正常队列的最大长度
        //arguments.put("x-max-length", 6);
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);

        //****************************************************************************************//
        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        //绑定交换机
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        //接收消息
        //接收消息的回调函数
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String messages = new String(message.getBody(), "UTF-8");
            if (messages.equals("info5")) {
                /**
                 * 拒绝消息
                 * 1.消息的标识
                 * 2.是否放回队列
                 */
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
                System.out.println("Consumer01接收到消息：" + messages + " ,此消息是被拒绝的");
            }else {
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                System.out.println("Consumer01接收到消息：" + messages);
            }

        };
        //取消消息的回调函数
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费者取消接收消息回调函数触发。。。。" + consumerTag);
        };
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, cancelCallback);
    }
}
