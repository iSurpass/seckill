package com.juniors.miaosha.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ 消息队列接收者
 * @author Juniors
 */
@Service
public class MQReceiver {

    public static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message){

        log.info("Receive Message :"+message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveTopic(String message){

        log.info("Receive topicQueue1 Message :"+message);
    }
    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveTopic1(String message){

        log.info("Receive topicQueue2 Message :"+message);
    }

    @RabbitListener(queues = MQConfig.FANOUT_QUEUE1)
    public void receiveFanout(String message){

        log.info("Receive fanoutQueue1 Message :"+message);
    }
    @RabbitListener(queues = MQConfig.FANOUT_QUEUE2)
    public void receiveFanout1(String message){

        log.info("Receive Topic2 Message :"+message);
    }
    @RabbitListener(queues = MQConfig.HEADERS_QUEUE1)
    public void receiveHeaders(byte[] msg){

        log.info("Receive HeadersQueue Message :"+new String(msg));
    }
}
