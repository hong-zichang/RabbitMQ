package com.zichang.rabbitmq.three;

/**
 * author: ZiChangHong
 * create-date: 2022/9/28 21:15
 **/

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zichang.rabbitmq.utils.RabbitMQUtils;
import com.zichang.rabbitmq.utils.SleepUtil;

/**
 * 消费者消费消息，消费者手动应答，未应答重新返回队列
 */
public class Work04 {
    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    //接收消息
    public static void main(String[] args) throws Exception {
        //利用工具类得到信道
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("C2线程应答比较慢......");

        //开启发布确认
        channel.confirmSelect();

        //设置不公平分发 1为不公平分发 >1为预取值
        //int prefetchCount = 1;
        int prefetchCount = 5;
        channel.basicQos(prefetchCount);

        //接收消息的回调函数
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //模拟复杂业务，睡一秒
            SleepUtil.sleep(30);
            System.out.println("C2接收到消息：" + new String(message.getBody(), "UTF-8"));
            //手动应答
            /**
             * 利用信道向交换机应答
             * 1.消息的标识
             * 2.是否批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        //设置手动应答
        /**
         * 获取消息
         * 1.队列名称
         * 2.是否自动应答
         * 3.接收消息的回调
         * 4.取消消息接收的回调
         */
        boolean autoAsk = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAsk, deliverCallback, consumerTag -> {
            System.out.println(consumerTag + "消息取消回调函数回调");
        });
    }
}
