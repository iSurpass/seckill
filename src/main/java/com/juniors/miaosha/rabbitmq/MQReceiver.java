package com.juniors.miaosha.rabbitmq;

import com.juniors.miaosha.domain.MiaoshaOrder;
import com.juniors.miaosha.domain.MiaoshaUser;
import com.juniors.miaosha.domain.OrderInfo;
import com.juniors.miaosha.redis.RedisService;
import com.juniors.miaosha.result.CodeMsg;
import com.juniors.miaosha.result.Result;
import com.juniors.miaosha.service.GoodsService;
import com.juniors.miaosha.service.MiaoshaService;
import com.juniors.miaosha.service.MiaoshaUserService;
import com.juniors.miaosha.service.OrderService;
import com.juniors.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ 消息队列接收者
 * @author Juniors
 */
@Service
public class MQReceiver {

    public static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receiveMS(String message){
        log.info("Receive MSMessage :"+message);
        MiaoshaMessage mm = RedisService.stringToBean(message,MiaoshaMessage.class);
        MiaoshaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();

        //判断数据库库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0){
            return;
        }

        //判断是否重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(),goodsId);
        if (order != null){
            return;
        }

        //减库存 下订单 写入秒杀订单 ----------事务性操作
        OrderInfo orderInfo = miaoshaService.miaosha(user,goods);

    }

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
