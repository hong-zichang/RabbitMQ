package com.zichang.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * author: ZiChangHong
 * create-date: 2022/9/28 15:32
 **/

//生产者。生产信息
public class Producer {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //发送消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置工厂IP
        factory.setHost("192.168.146.100");
        //设置用户名
        factory.setUsername("admin");
        //设置密码
        factory.setPassword("123");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        /**
         * 创建一个队列
         * 1.队列名称
         * 2.队列里的消息是否持久化（磁盘）， 默认情况消息存放在内存中
         * 3.该队列是否只供一个消费者进行消费，是否进行消息共享，true可以供给多个消费者消费 false只能给一个消费者消费，不共享
         * 4.是否自动删除 最后一个消费者断开连接后，该队列是否自动删除 true自动删除，FALSE不自动删除
         * 5.其它参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!";

        /**
         * 发送一个消息
         * 1.发送到哪个交换机
         * 2.路由的key值是哪个
         * 3.其它参数信息
         * 4.发送消息的消息体
         */
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                int sleepTime = (int) (Math.random() * 1000);
                try {
                    channel.basicPublish("", QUEUE_NAME, null, (message + "*" + i).getBytes(StandardCharsets.UTF_8));
                    System.out.println("消息发送完毕！");
                    Thread.sleep(sleepTime);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
//        System.out.println("消息发送完毕！");
    }
}
