package com.juniors.miaosha.controller;

import com.juniors.miaosha.redis.RedisService;
import com.juniors.miaosha.result.Result;
import com.juniors.miaosha.service.MiaoshaUserService;
import com.juniors.miaosha.service.UserService;
import com.juniors.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 示例Controller类
 * @author Juniors
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    // log4j 日志
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    //注入userService的Bean对象
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService miaoshaUserService;

    /**
     * 跳转登录页
     * @return
     */
    @RequestMapping(path = "/toLogin")
    public String toLogin(){
        return "login";
    }

    /**
     * 执行登录操作
     * @param response
     * @param loginVo
     * @return
     */
    @RequestMapping(path = "/doLogin")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){

        logger.info(loginVo.toString());
        //登录
        miaoshaUserService.login(response,loginVo);
        return Result.success(true);
    }
}
