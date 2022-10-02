package com.zichang.rabbitmq.five;

/**
 * author: ZiChangHong
 * create-date: 2022/10/2 14:06
 **/

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zichang.rabbitmq.utils.RabbitMQUtils;

/**
 * 消息接收 队列一
 */
public class ReceiveLogs01 {

    //声明交换机名称
    public static String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明一个交换机 1.交换机名称 2.交换机类型（fanout 扇出 也即发布订阅模式）
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        /**
         * 声明一个临时队列
         * 当消费者断开与队列的连接时，队列会自动删除
         */
        String queue_name = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机和队列
         */
        channel.queueBind(queue_name, EXCHANGE_NAME, "");
        System.out.println("等待接收消息，把消息打印在屏幕上。。。。。。");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs01控制台打印接收到的消息：" + new String(message.getBody(), "UTF-8"));
        };
        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("取消消息的回调函数：" + consumerTag);
        };
        channel.basicConsume(queue_name, true, deliverCallback, cancelCallback);
    }
}
