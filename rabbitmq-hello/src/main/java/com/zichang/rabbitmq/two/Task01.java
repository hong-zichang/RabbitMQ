package com.zichang.rabbitmq.two;

/**
 * author: ZiChangHong
 * create-date: 2022/9/28 20:44
 **/

import com.rabbitmq.client.Channel;
import com.zichang.rabbitmq.utils.RabbitMQUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 生产者类，生产消息
 */
public class Task01 {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //发现大量消息
    public static void main(String[] args) throws Exception {
        //利用工具类得到信道
        Channel channel = RabbitMQUtils.getChannel();
        //队列声明
        /**
         * 创建一个队列
         * 1.队列名称
         * 2.队列里的消息是否持久化（磁盘）， 默认情况消息存放在内存中
         * 3.该队列是否只供一个消费者进行消费，是否进行消息共享，true可以供给多个消费者消费 false只能给一个消费者消费，不共享
         * 4.是否自动删除 最后一个消费者断开连接后，该队列是否自动删除 true自动删除，FALSE不自动删除
         * 5.其它参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //从控制台当中接收消息
        Scanner in = new Scanner(System.in);
        while (in.hasNext()){
            channel.basicPublish("", QUEUE_NAME, null, in.next().getBytes(StandardCharsets.UTF_8));
        }
    }

}
