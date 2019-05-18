package com.juniors.miaosha.controller;

import com.juniors.miaosha.domain.MiaoshaOrder;
import com.juniors.miaosha.domain.MiaoshaUser;
import com.juniors.miaosha.domain.OrderInfo;
import com.juniors.miaosha.redis.RedisService;
import com.juniors.miaosha.result.CodeMsg;
import com.juniors.miaosha.service.GoodsService;
import com.juniors.miaosha.service.MiaoshaService;
import com.juniors.miaosha.service.MiaoshaUserService;
import com.juniors.miaosha.service.OrderService;
import com.juniors.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 示例Controller类
 * @author Juniors
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

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

    /**
     * ---------JMeter压测初试-----------
     * QPS:1360
     * 5000 * 10
     * ---------------------------
     * 执行秒杀动作
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(path = "/doMiaosha")
    public String doMiaosha(Model model,MiaoshaUser user,
        @RequestParam("goodsId")long goodsId){

        //引入用户对象模型
        model.addAttribute("user",user);
        if (user == null){
            return "login";
        }

        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0){
            model.addAttribute("errmsg",CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }

        //判断是否重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(),goodsId);
        if (order != null){
            model.addAttribute("errmsg",CodeMsg.REPEAT_ERROR.getMsg());
            return "miaosha_fail";
        }

        //减库存 下订单 写入秒杀订单 ----------事务性操作
        OrderInfo orderInfo = miaoshaService.miaosha(user,goods);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goods);

        return "order_detail";
    }
}
