package com.zichang.rabbitmq.four;

/**
 * author: ZiChangHong
 * create-date: 2022/10/1 23:02
 **/

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.zichang.rabbitmq.utils.RabbitMQUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认模式
 * 使用的时间 比较哪种方式是比较好的
 * 1.单个确认
 * 2.批量确认
 * 3.异步批量确认
 */
public class ConfirmMessage {
    //批量发消息的个数
    public static int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //1.单个确认
        //ConfirmMessage.publicMessageIndividually();//使用单个确认发送1000条消息，一共花费了691ms
        //2.批量确认
        //ConfirmMessage.publicMessageByBatch();//使用批量确认发送1000条消息，一共花费了145ms
        //3.异步批量确认
        ConfirmMessage.publicMessageByAsync();//使用批量确认发送1000条消息，一共花费了50ms

    }

    //单个确认
    public static void publicMessageIndividually() throws Exception {
        //获取信道
        Channel channel = RabbitMQUtils.getChannel();
        //队列名称
        String QUEUE_NAME = UUID.randomUUID().toString();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //发布消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            channel.basicPublish("", QUEUE_NAME, null, (i + "").getBytes(StandardCharsets.UTF_8));
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功！" + i);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("使用单个确认发送" + MESSAGE_COUNT + "条消息，一共花费了" + (end - begin) + "ms");
    }

    //批量确认
    public static void publicMessageByBatch() throws Exception{
        //获取信道
        Channel channel = RabbitMQUtils.getChannel();
        //队列名称
        String QUEUE_NAME = UUID.randomUUID().toString();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //批量确认消息大小
        int batchSize = 100;
        //发布消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            channel.basicPublish("", QUEUE_NAME, null, (i + "").getBytes(StandardCharsets.UTF_8));
            if ((i+1)%batchSize==0) {
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    System.out.println("消息发送成功！");
                }
            }

        }
        long end = System.currentTimeMillis();
        System.out.println("使用批量确认发送" + MESSAGE_COUNT + "条消息，一共花费了" + (end - begin) + "ms");
    }

    //异步确认
    public static void publicMessageByAsync() throws Exception{
        //获取信道
        Channel channel = RabbitMQUtils.getChannel();
        //队列名称
        String QUEUE_NAME = UUID.randomUUID().toString();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        /**
         * 准备一个线程安全有序的一个哈希表,适用于高并发情况下
         * 1.轻松的将序号与消息关联
         * 2.轻松批量删除条目，只要给到序号
         * 3.支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> outStandingConfirms =
                new ConcurrentSkipListMap<>();

        //开始时间
        long begin = System.currentTimeMillis();

        /**
         * 消息发送成功的回调函数
         * 1.消息的标识
         * 2.是否为批量确认
         */
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            /**
             * 批量确认，批量删除
             * 否则，单条删除
             */
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmMessage
                        = outStandingConfirms.headMap(deliveryTag);
                confirmMessage.clear();
            }else {
                outStandingConfirms.remove(deliveryTag);
            }
            System.out.println("消息发送成功：" + deliveryTag);
        };

        /**
         * 消息发送失败的回调
         * 1.消息的标识
         * 2.是否为批量确认
         */
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            String message = outStandingConfirms.get(deliveryTag);
            System.out.println("消息发送失败：" + message + " 未确认消息的tag：" + deliveryTag);
        };

        /**
         * 添加监听器，异步监听消息发送成功与否
         * 1.消息成功发送的回调函数
         * 2.消息未成功发送的回调函数
         */
        channel.addConfirmListener(ackCallback, nackCallback);

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息" + i;
            channel.basicPublish("", QUEUE_NAME, false, null, message.getBytes(StandardCharsets.UTF_8));
            //1.将所有消息记录下来
            outStandingConfirms.put(channel.getNextPublishSeqNo() - 1, message);
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("使用批量确认发送" + MESSAGE_COUNT + "条消息，一共花费了" + (end - begin) + "ms");
    }
}
