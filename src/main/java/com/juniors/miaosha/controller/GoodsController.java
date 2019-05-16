package com.juniors.miaosha.controller;

import com.juniors.miaosha.domain.MiaoshaUser;
import com.juniors.miaosha.domain.User;
import com.juniors.miaosha.redis.RedisService;
import com.juniors.miaosha.result.Result;
import com.juniors.miaosha.service.MiaoshaUserService;
import com.juniors.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

    @RequestMapping(path = "/toList")
    public String toList(Model model,
                         MiaoshaUser user){
        model.addAttribute("user",user);
        return "goods_list";
    }

    @RequestMapping(path = "/doLogin")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){

        logger.info(loginVo.toString());
        //登录
        miaoshaUserService.login(response,loginVo);
        return Result.success(true);
    }
}
