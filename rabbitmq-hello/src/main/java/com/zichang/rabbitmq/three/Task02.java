package com.zichang.rabbitmq.three;

/**
 * author: ZiChangHong
 * create-date: 2022/9/28 21:06
 **/

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.zichang.rabbitmq.utils.RabbitMQUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 生产者生产消息，消费者手动应答，未应答重新返回队列
 * 持久化队列，即使重启，队列也存在。例如one中的hello和three中的ack_queue
 */
public class Task02 {

    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    //发送大量消息
    public static void main(String[] args) throws Exception {
        //利用工具类获得通道
        Channel channel = RabbitMQUtils.getChannel();

        //创建队列,队列名 是否持久化 是否共享 是否删除 其他参数
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.nextLine();
            System.out.println("生产者生产消息：" + message);
            //设置生产者发送消息为持久化消息(保存在磁盘当中，但这种并不是一定不会丢失) MessageProperties.PERSISTENT_TEXT_PLAIN
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
