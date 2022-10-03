package com.zichang.springboot_rabbitmq.config;

/**
 * author: ZiChangHong
 * create-date: 2022/10/3 16:55
 **/

import io.swagger.annotations.ApiParam;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 过期时间队列 配置文件类
 */

@Configuration
public class TTLQueueConfig {
    //普通交换机的名称
    public static final String NORMAL_EXCHANGE = "X";
    //普通的队列名称
    public static final String NORMAL_QUEUE_A = "QA";
    public static final String NORMAL_QUEUE_B = "QB";
    //死信交换机的名称
    public static final String DEAD_EXCHANGE = "Y";
    //死信的队列名称
    public static final String DEAD_QUEUE = "QD";

    //声明x交换机
    @Bean("xExchange")
    @ApiParam("正常交换机X")
    public DirectExchange xExchange(){
        return new DirectExchange(NORMAL_EXCHANGE);
    }

    //声明y交换机
    @Bean("yExchange")
    @ApiParam("死信交换机Y")
    public DirectExchange yExchange(){
        return new DirectExchange(DEAD_EXCHANGE);
    }

    @Bean("queueA")
    @ApiParam("正常队列A")
    public Queue queueA(){
        Map<String, Object> prop = new HashMap<>();
        /**
         * 正常队列需要设置
         * 1.死信交换机
         * 2.死信routing_key
         * 3.过期时间ttl
         */
        prop.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        prop.put("x-dead-letter-routing-key", "YD");
        prop.put("x-message-ttl", 10000);
        return QueueBuilder.durable(NORMAL_QUEUE_A).withArguments(prop).build();
    }

    @Bean("queueB")
    @ApiParam("正常队列B")
    public Queue queueB(){
        Map<String, Object> prop = new HashMap<>();
        /**
         * 正常队列需要设置
         * 1.死信交换机
         * 2.死信routing_key
         * 3.过期时间ttl
         */
        prop.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        prop.put("x-dead-letter-routing-key", "YD");
        prop.put("x-message-ttl", 40000);
        return QueueBuilder.durable(NORMAL_QUEUE_B).withArguments(prop).build();
    }

    @Bean("queueD")
    @ApiParam("死信队列D")
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_QUEUE).build();
    }

    //绑定
    @Bean
    @ApiParam("队列A绑定交换机X")
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    @Bean
    @ApiParam("队列B绑定交换机X")
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean
    @ApiParam("队列D绑定交换机Y")
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }
}
