package com.juniors.miaosha.controller;

import com.juniors.miaosha.domain.MiaoshaUser;
import com.juniors.miaosha.redis.RedisService;
import com.juniors.miaosha.result.Result;
import com.juniors.miaosha.service.MiaoshaUserService;
import com.juniors.miaosha.service.UserService;
import com.juniors.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 示例Controller类
 * @author Juniors
 */
@Controller
@RequestMapping("/user")
public class UserController {

    // log4j 日志
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    //注入userService的Bean对象
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService miaoshaUserService;

    /**
     * JMeter压测Test
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(path = "/info")
    public Result<MiaoshaUser> toList(Model model,MiaoshaUser user){
        return Result.success(user);
    }

}
