package com.zichang.rabbitmq.seven;

/**
 * author: ZiChangHong
 * create-date: 2022/10/2 15:02
 **/

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zichang.rabbitmq.utils.RabbitMQUtils;

/**
 * 主题交换机
 * 消费者C1
 */
public class ReceiveLogsTopic01 {
    //声明交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明一个队列
        String queue_name = "Q1";
        channel.queueDeclare(queue_name, false, false, false, null);
        channel.queueBind(queue_name, EXCHANGE_NAME, "*.orange.*");
        System.out.println("等待接收消息......");


        //接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsTopic01接收到消息:" + new String(message.getBody(), "UTF-8") +
                    " 发送方为：" + message.getEnvelope().getRoutingKey());
        };

        //取消消息的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费者取消接收消息的回调函数触发：" + consumerTag);
        };

        //接收消息
        channel.basicConsume(queue_name, true, deliverCallback, cancelCallback);
    }
}
