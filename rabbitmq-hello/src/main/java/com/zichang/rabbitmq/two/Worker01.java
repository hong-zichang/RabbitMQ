package com.zichang.rabbitmq.two;

/**
 * author: ZiChangHong
 * create-date: 2022/9/28 20:32
 **/

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zichang.rabbitmq.utils.RabbitMQUtils;

/**
 * 这是一个工作线程，相当于之前的消费者
 *
 */
public class Worker01 {

    //队列名称
    public static final String QUEUE_NAME = "hello";

    //接收消息
    public static void main(String[] args) throws Exception {
        //利用工具类得到信道
        Channel channel = RabbitMQUtils.getChannel();

        //消息被接收时的回调函数
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到的消息为：" + new String(message.getBody()));
        };

        //消息取消接收时的回调函数
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消费者取消消费的接口回调逻辑");
        };

        //消费者消费消息
        System.out.println("C2等待接收消息。。。。。。");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
