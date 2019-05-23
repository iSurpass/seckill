package com.juniors.miaosha.controller;

import com.juniors.miaosha.domain.MiaoshaUser;
import com.juniors.miaosha.domain.OrderInfo;
import com.juniors.miaosha.redis.RedisService;
import com.juniors.miaosha.result.CodeMsg;
import com.juniors.miaosha.result.Result;
import com.juniors.miaosha.service.GoodsService;
import com.juniors.miaosha.service.MiaoshaUserService;
import com.juniors.miaosha.service.OrderService;
import com.juniors.miaosha.service.UserService;
import com.juniors.miaosha.vo.GoodsVo;
import com.juniors.miaosha.vo.OrderDetailVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 示例Controller类
 * @author Juniors
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    // log4j 日志
    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    //注入userService的Bean对象
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    /**
     * JMeter压测Test
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(path = "/detail")
    @ResponseBody
  //@Notuser拦截器优化
    public Result<OrderDetailVo> info(Model model, MiaoshaUser user,
                                      @RequestParam("orderId")long orderId){
        //用户判空可优化---拦截器注释
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        System.out.println(orderId);
        OrderInfo order = orderService.getOrderId(orderId);
        if (order == null){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setGoods(goodsVo);
        vo.setOrder(order);

        return Result.success(vo);
    }

}
