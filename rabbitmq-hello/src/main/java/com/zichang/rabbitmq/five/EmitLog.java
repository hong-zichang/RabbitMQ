package com.zichang.rabbitmq.five;

/**
 * author: ZiChangHong
 * create-date: 2022/10/2 14:18
 **/

import com.rabbitmq.client.Channel;
import com.zichang.rabbitmq.utils.RabbitMQUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 发消息 交换机 发布订阅模式
 */
public class EmitLog {
    //声明交换机名称
    public static String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String message = in.nextLine();
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
