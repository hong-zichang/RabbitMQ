package com.zichang.rabbitmq.nice;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zichang.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * author: ZiChangHong
 * create-date: 2022/9/28 15:32
 **/

/**
 * 生产者。生产信息 优先级队列 优先级一样就按发送的时间前后排序
 * info*0 9
 * info*2 9
 * info*9 9
 * info*5 8
 * info*6 8
 * info*8 6
 * info*4 4
 * info*1 1
 * info*3 0
 * info*7 0
 */
public class Producer {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //发送消息
    public static void main(String[] args) throws Exception {
        //获取信道
        Channel channel = RabbitMQUtils.getChannel();
        /**
         * 创建一个队列
         * 1.队列名称
         * 2.队列里的消息是否持久化（磁盘）， 默认情况消息存放在内存中
         * 3.该队列是否只供一个消费者进行消费，是否进行消息共享，true可以供给多个消费者消费 false只能给一个消费者消费，不共享
         * 4.是否自动删除 最后一个消费者断开连接后，该队列是否自动删除 true自动删除，FALSE不自动删除
         * 5.其它参数
         */
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority", 10);//设置最大优先级 可以为0-255之间 但企业基本都是在0-10之间
        channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);


        /**
         * 发送一个消息
         * 1.发送到哪个交换机
         * 2.路由的key值是哪个
         * 3.其它参数信息
         * 4.发送消息的消息体
         */
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                int sleepTime = (int) (Math.random() * 1000);
                try {
                    int priority = (int)(Math.random() * 10);
                    AMQP.BasicProperties props = new AMQP.BasicProperties().builder().priority(priority).build();
                    String msg = "info*" + i + " " + priority;
                    channel.basicPublish("", QUEUE_NAME, props, msg.getBytes(StandardCharsets.UTF_8));
                    System.out.println("消息发送完毕！");
                    Thread.sleep(sleepTime);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
