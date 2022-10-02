package com.zichang.rabbitmq.six;

/**
 * author: ZiChangHong
 * create-date: 2022/10/2 14:37
 **/

import com.rabbitmq.client.Channel;
import com.zichang.rabbitmq.utils.RabbitMQUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 消息发送方 生产者
 */
public class DirectLogs {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();

        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String[] message_type = in.nextLine().split(" ");
            channel.basicPublish(EXCHANGE_NAME, message_type[1], null, message_type[0].getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者将消息：" + message_type[0] + "发送给：" + message_type[1]);
        }
    }
}
