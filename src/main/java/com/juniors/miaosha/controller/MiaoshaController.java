package com.juniors.miaosha.controller;

import com.juniors.miaosha.domain.MiaoshaOrder;
import com.juniors.miaosha.domain.MiaoshaUser;
import com.juniors.miaosha.domain.OrderInfo;
import com.juniors.miaosha.rabbitmq.MQSender;
import com.juniors.miaosha.rabbitmq.MiaoshaMessage;
import com.juniors.miaosha.redis.GoodsKey;
import com.juniors.miaosha.redis.RedisService;
import com.juniors.miaosha.result.CodeMsg;
import com.juniors.miaosha.result.Result;
import com.juniors.miaosha.service.GoodsService;
import com.juniors.miaosha.service.MiaoshaService;
import com.juniors.miaosha.service.MiaoshaUserService;
import com.juniors.miaosha.service.OrderService;
import com.juniors.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 示例Controller类
 * @author Juniors
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

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

    @Autowired
    MQSender mqSender;

    private Map<Long,Boolean> localOverMap = new HashMap<>();

    /**
     * 系统初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.lisGoodsVo();
        if (goodsVoList == null){
            return;
        }
        for (GoodsVo vo:goodsVoList){
            redisService.set(GoodsKey.getGoodsStock,""+vo.getId(),vo.getStockCount());
            localOverMap.put(vo.getId(),false);
        }
    }

    /**
     * 利用消息队列
     * 优化秒杀接口---------v3.0
     * QPS------2114----------
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/doMiaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doMiaoshaPro(Model model, MiaoshaUser user,
                                       @RequestParam("goodsId")long goodsId){

        //引入用户对象模型
        model.addAttribute("user",user);
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //内存标记，减少对redis访问------优化点
        boolean over = localOverMap.get(goodsId);
        if (over){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //优化后可以先从初始化Redis中库存数据里取并---预减库存----
        long stock = redisService.decr(GoodsKey.getGoodsStock,""+goodsId);
        if (stock < 0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //判断是否重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(),goodsId);
        if (order != null){
            return Result.error(CodeMsg.REPEAT_ERROR);
        }

        //入队
        MiaoshaMessage mm = new MiaoshaMessage(user,goodsId);
        mqSender.sendMiaoshaMessage(mm);

        return Result.success(0);//排队中
    }


    /**
     * 优化秒杀接口------------v2.0
     * @param model
     * @param user
     * @param goodsId
     * @return
     * ----------面试------------
     * GET 幂等
     * POST 安全
     */
    @RequestMapping(value = "/doMiaosha1",method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> doMiaosha(Model model, MiaoshaUser user,
                            @RequestParam("goodsId")long goodsId){

        //引入用户对象模型
        model.addAttribute("user",user);
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        /*判断是否重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(),goodsId);
        if (order != null){
            return Result.error(CodeMsg.REPEAT_ERROR);
        }*/

        //减库存 下订单 写入秒杀订单 ----------事务性操作
        OrderInfo orderInfo = miaoshaService.miaosha(user,goods);

        return Result.success(orderInfo);
    }

    /**
     * ---------JMeter压测初试-----------
     * QPS:1360
     * 5000 * 10
     * ---------------------------
     * 执行秒杀动作--------------v1.0
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(path = "/doMiaosha2")
    public String doMiaosha1(Model model,MiaoshaUser user,
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

    /**
     * 对秒杀结果进行轮询
     * @param model
     * @param user
     * @param goodsId
     * @return orderId : 成功
     *         1  :  秒杀失败
     *         0  :  排队中
     */
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser user,
                                       @RequestParam("goodsId")long goodsId) {

        //引入用户对象模型
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(),goodsId);
        return Result.success(result);
    }
}
