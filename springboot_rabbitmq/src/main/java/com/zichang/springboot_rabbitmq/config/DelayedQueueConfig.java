package com.zichang.springboot_rabbitmq.config;

import io.swagger.annotations.ApiParam;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * author: ZiChangHong
 * create-date: 2022/10/3 23:22
 **/

/**
 * 基于插件的延迟队列配置类
 */
@Configuration
public class DelayedQueueConfig {
    //延迟队列名称
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //延迟交换机名称
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //延迟routing_key
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    @Bean("delayedQueue")
    @ApiParam("延迟队列声明")
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE_NAME);
    }

    @Bean("delayedExchange")
    @ApiParam("延迟交换机声明")
    public CustomExchange delayedExchange(){
        Map<String, Object> args = new HashMap<>();
        //设置延迟交换机的类型为直接类型
        args.put("x-delayed-type", "direct");
        /**
         * 1.交换机名称
         * 2.交换机类型
         * 3.是否持久化
         * 4.是否自动删除
         * 5.其他参数
         */
        String EXCHANGE_TYPE = "x-delayed-message";
        return new CustomExchange(DELAYED_EXCHANGE_NAME, EXCHANGE_TYPE, true, false, args);
    }

    @Bean
    @ApiParam("延迟队列与延迟交换机绑定")
    public Binding delayedQueueBindingDelayedExchange(@Qualifier("delayedQueue") Queue queue,
                                                      @Qualifier("delayedExchange") CustomExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
