package com.zichang.rabbitmq.utils;

/**
 * author: ZiChangHong
 * create-date: 2022/9/28 20:29
 **/

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 此类为连接工厂创建信道的工具类
 */
public class RabbitMQUtils {
    //得到一个连接的信道Channel
    public static Channel getChannel() throws Exception{
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
        return channel;
    }
}
