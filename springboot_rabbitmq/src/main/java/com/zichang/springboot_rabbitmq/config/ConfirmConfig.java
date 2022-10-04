package com.zichang.springboot_rabbitmq.config;

/**
 * author: ZiChangHong
 * create-date: 2022/10/4 15:18
 **/

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类 确认高级
 *
 * 备份交换机
 */
@Configuration
public class ConfirmConfig {
    //交换机名称
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    //队列名称
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    //routing-key
    public static final String CONFIRM_ROUTING_KEY = "key1";
    //备份交换机
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    //回退队列
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    //报警队列
    public static final String WARNING_QUEUE_NAME = "warning.queue";


    @Bean("confirmExchange")
    public DirectExchange confirmExchange(){
        return ExchangeBuilder
                .directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                .alternate(BACKUP_EXCHANGE_NAME)
                .build();
    }

    @Bean("confirmQueue")
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    @Bean
    public Binding confirmQueueBindingConfirmExchange(@Qualifier("confirmQueue") Queue queue,
                                                      @Qualifier("confirmExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(CONFIRM_ROUTING_KEY);
    }

    @Bean
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    @Bean
    public Queue backupQueue(){
        return new Queue(BACKUP_QUEUE_NAME);
    }

    @Bean
    Queue warningQueue(){
        return new Queue(WARNING_QUEUE_NAME);
    }

    @Bean
    public Binding backupQueueBindingBackupExchange(@Qualifier("backupQueue") Queue queue,
                                                    @Qualifier("backupExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding warningQueueBindingBackupExchange(@Qualifier("warningQueue") Queue queue,
                                                     @Qualifier("backupExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }
}
