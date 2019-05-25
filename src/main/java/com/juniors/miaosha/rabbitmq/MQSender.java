package com.juniors.miaosha.rabbitmq;

import com.juniors.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ 消息队列接收者
 * @author Juniors
 */
@Service
public class MQSender {

    public static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendMiaoshaMessage(MiaoshaMessage mm) {

        String message = RedisService.beanToString(mm);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE,message);
    }

    public void send(Object message){

        //将要发送的Message要先将其Bean对象转换String
        String msg = RedisService.beanToString(message);
        log.info("Send message :" + message);
        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
    }

    public void sendTopic(Object message){

        String msg = RedisService.beanToString(message);
        log.info("Send message :"+message);
        amqpTemplate.convertAndSend(MQConfig.EXCHANGE_QUEUE,MQConfig.ROUTING_KEY1,msg+1);
        amqpTemplate.convertAndSend(MQConfig.EXCHANGE_QUEUE,MQConfig.ROUTING_KEY2,msg+2);
    }

    public void sendFanout(Object message){

        String msg = RedisService.beanToString(message);
        log.info("Send message :"+message);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg+1);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg+2);
    }

    public void sendHeaders(Object message){

        String msg = RedisService.beanToString(message);
        log.info("Send message :"+message);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("key1","Juniors");
        properties.setHeader("key2","Harden");
        Message message1 = new Message(msg.getBytes(),properties);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE,"",message1);
    }

}
