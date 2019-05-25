package com.juniors.miaosha.service;

import com.juniors.miaosha.dao.GoodsDao;
import com.juniors.miaosha.domain.Goods;
import com.juniors.miaosha.domain.MiaoshaOrder;
import com.juniors.miaosha.domain.MiaoshaUser;
import com.juniors.miaosha.domain.OrderInfo;
import com.juniors.miaosha.redis.MiaoshaKey;
import com.juniors.miaosha.redis.RedisService;
import com.juniors.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Juniors
 */
@Service
public class MiaoshaService {

    //如果需要引入不是本类的 Dao 时，不应该直接引入，应该引入其 Service，进行间接引入 ------方便管理
    //@Autowired
    //GoodsDao goodsDao;
    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        if (success){
            //order_info miaosha_order
            return orderService.createOrder(user,goods);
        }else {
            setGoodsOver(goods.getId());
            return null;
        }
    }


    public long getMiaoshaResult(Long userId, long goodsId) {

        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(userId,goodsId);
        if (order != null){
            return order.getId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver){
                return -1;
            }else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long id) {
        redisService.set(MiaoshaKey.isGoodsOver,""+id,true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver,""+goodsId);
    }
}
