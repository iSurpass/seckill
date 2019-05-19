package com.juniors.miaosha.controller;

import com.juniors.miaosha.domain.MiaoshaUser;
import com.juniors.miaosha.domain.User;
import com.juniors.miaosha.redis.GoodsKey;
import com.juniors.miaosha.redis.RedisService;
import com.juniors.miaosha.result.Result;
import com.juniors.miaosha.service.GoodsService;
import com.juniors.miaosha.service.MiaoshaUserService;
import com.juniors.miaosha.vo.GoodsDetailVo;
import com.juniors.miaosha.vo.GoodsVo;
import com.juniors.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 商品Controller类
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

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    /**
     * ------------ JMeter 压测初试 -------------
     *          QPS    :  1267
     *          Load   :  15
     *          COUNT :   5000 * 10
     *       **** MySql 占比高 *****
     * -----------------------------------------
     * ----------- Redis 优化压测复试 -------------
     *         QPS    :  2884
     *         Load   :  5
     *         COUNT :   5000 * 10
     *     **** MySql 占比大大减小 ****
     * -----------------------------------------
     * 跳转商品列表页-------------实现页面静态化以提高高并发下访问速度
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(path = "/toList",produces = "text/html")
    @ResponseBody
    public String toList(Model model,MiaoshaUser user,
        HttpServletRequest request,HttpServletResponse response){
        //引入用户对象模型
        model.addAttribute("user",user);

        //优先取缓存，以减少对数据库的访问，提高页面访问速度
        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }

        //再从数据库里查询
        List<GoodsVo> goodsList = goodsService.lisGoodsVo();
        model.addAttribute("goodsList",goodsList);

        WebContext context = new WebContext(request,response,request.getServletContext(),
                request.getLocale(),model.asMap());

        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",context);
        if (!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        System.out.println("111111111111");
        return html;
    }

    /**
     * 跳转相应商品详情页
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/toDetail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail(Model model, MiaoshaUser user,
                           @PathVariable("goodsId")long goodsId){

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        //获取系统当前时间
        long now = System.currentTimeMillis();
        //秒杀商品状态
        int miaoshaStatus = 0;
        //距离秒杀剩余声音
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

        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoodsVo(goods);
        vo.setMiaoshaStatus(miaoshaStatus);
        vo.setRemainSecond(remainSecond);
        vo.setUser(user);
        System.out.println(vo.getGoodsVo().getStartDate());
        System.out.println(vo.getRemainSecond());

        //返回客户端直接数据而不是页面
        return Result.success(vo);
    }

    @RequestMapping(path = "/toDetail1/{goodsId}",produces = "text/html")
    @ResponseBody
    public String toDetail2(Model model, MiaoshaUser user,
                           @PathVariable("goodsId")long goodsId,
                           HttpServletResponse response,HttpServletRequest request){
        //引入用户对象模型
        model.addAttribute("user",user);

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        //获取系统当前时间
        long now = System.currentTimeMillis();

        //秒杀商品状态
        int miaoshaStatus = 0;
        //距离秒杀剩余声音
        int remainSecond = 0;

        if (now < startAt){ //秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSecond = (int) ((startAt - now)/1000);
        }else if (now > endAt){ // 秒杀已经结束
            miaoshaStatus = 2;
            remainSecond = -1;
        }else {     //秒杀正在进行中
            miaoshaStatus = 1;
            remainSecond = 0;
        }

        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSecond",remainSecond);

        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList,""+goodsId,String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        //手动渲染
        WebContext context = new WebContext(request,response,request.getServletContext(),
                request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",context);
        if (!StringUtils.isEmpty(html)){
            //缓存页面的区别就是不同goodsId标记不同的key
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);
        }

        return html;
    }
}
