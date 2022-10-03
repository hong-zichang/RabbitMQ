package com.zichang.springboot_rabbitmq.config;

import io.swagger.annotations.ApiParam;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * author: ZiChangHong
 * create-date: 2022/10/3 22:31
 **/

/**
 * 可变ttl的队列声明配置类
 */
@Component
public class MsgTtlQueueConfig {
    //正常队列C的名称
    public static final String NORMAL_QUEUE_C = "QC";
    //死信交换机的名称
    public static final String dead_exchange = "Y";

    @Bean("queueC")
    @ApiParam("正常队列C")
    public Queue queueC(){
        Map<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", dead_exchange);
        map.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(NORMAL_QUEUE_C).withArguments(map).build();
    }

    @Bean
    @ApiParam("队列C绑定交换机X")
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }
}
