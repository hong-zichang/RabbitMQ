package com.zichang.rabbitmq.three;

/**
 * author: ZiChangHong
 * create-date: 2022/9/28 21:06
 **/

import com.rabbitmq.client.Channel;
import com.zichang.rabbitmq.utils.RabbitMQUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 生产者生产消息，消费者手动应答，未应答重新返回队列
 */
public class Task02 {

    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    //发送大量消息
    public static void main(String[] args) throws Exception {
        //利用工具类获得通道
        Channel channel = RabbitMQUtils.getChannel();
        //创建队列,队列名 是否持久化 是否共享 是否删除
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.nextLine();
            System.out.println("生产者生产消息：" + message);
            channel.basicPublish("", TASK_QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
