package com.juniors.miaosha.controller;

import com.juniors.miaosha.domain.MiaoshaUser;
import com.juniors.miaosha.domain.User;
import com.juniors.miaosha.redis.RedisService;
import com.juniors.miaosha.result.Result;
import com.juniors.miaosha.service.GoodsService;
import com.juniors.miaosha.service.MiaoshaUserService;
import com.juniors.miaosha.vo.GoodsVo;
import com.juniors.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 示例Controller类
 * @author Juniors
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    // log4j 日志
    private static Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping(path = "/toList")
    public String toList(Model model,
                         MiaoshaUser user){
        //引入用户对象模型
        model.addAttribute("user",user);
        //引入秒杀商品列表对象
        List<GoodsVo> goodsList = goodsService.lisGoodsVo();
        model.addAttribute("goodsList",goodsList);

        return "goods_list";
    }

    @RequestMapping(path = "/toDetail/{goodsId}")
    public String toDetail(Model model, MiaoshaUser user,
                           @PathVariable("goodsId")long goodsId){
        //引入用户对象模型
        model.addAttribute("user",user);

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSecond = 0;

        if (now < startAt){ //秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSecond = (int) ((startAt - now)/1000);
        }else if (now > startAt){ // 秒杀已经结束
            miaoshaStatus = 2;
            remainSecond = -1;
        }else {     //秒杀正在进行中
            miaoshaStatus = 1;
            remainSecond = 0;
        }

        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSecond",remainSecond);

        return "goods_detail";
    }
}
