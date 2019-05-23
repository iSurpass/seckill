package com.juniors.miaosha.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * RabbitMQ配置信息
 * @author Juniors
 */
@Configuration
public class MQConfig {

    //
    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "topicQueue1";
    public static final String TOPIC_QUEUE2 = "topicQueue2";
    public static final String FANOUT_QUEUE1 = "fanoutQueue1";
    public static final String FANOUT_QUEUE2 = "fanoutQueue2";
    public static final String HEADERS_QUEUE1 = "headersQueue2";

    public static final String FANOUT_EXCHANGE = "fanoutExchange";
    public static final String EXCHANGE_QUEUE = "exchangeQueue";
    public static final String HEADERS_EXCHANGE = "headersExchange";

    public static final String ROUTING_KEY1 = "routingKey1";
    public static final String ROUTING_KEY2 = "routingKey#";

    /**
     * Direct 模式 -----最简单的交换机Exchange模式
     * 按照routingKey分发到指定队列
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue(QUEUE,true);
    }

    /**
     * Topic 模式 -----四种交换机Exchange模式之一
     * 多关键字匹配
     * @return
     */
    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE1,true);
    }
    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE2,true);

    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE_QUEUE);
    }
    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUTING_KEY1);
    }
    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(ROUTING_KEY2);
    }

    /**
     * Fanout 模式 -----四种交换机Exchange模式之一
     * 将消息分发到所有绑定队列，无RoutingKey概念
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }
    @Bean
    public Queue fanoutQueue1(){
        return new Queue(FANOUT_QUEUE1,true);
    }
    @Bean
    public Queue fanoutQueue2(){
        return new Queue(FANOUT_QUEUE2,true);
    }
    @Bean
    public Binding fanoutBinding1(){
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBinding2(){
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
    }

    /**
     * Headers 模式 -----四种交换机Exchange模式之一
     * @return
     */
    @Bean
    public HeadersExchange headersExchange(){ return new HeadersExchange(HEADERS_EXCHANGE); }
    @Bean
    public Queue headersQueue1(){
        return new Queue(HEADERS_QUEUE1,true);
    }
    @Bean
    public Binding headersBinding1(){

        HashMap<String,Object> map = new HashMap<>();
        map.put("key1","Juniors");
        map.put("key2","Harden");
        return BindingBuilder.bind(headersQueue1()).to(headersExchange()).whereAll(map).match();
    }

}
