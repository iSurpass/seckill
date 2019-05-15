package com.juniors.miaosha.controller;

import com.juniors.miaosha.Util.VaildatorUtil;
import com.juniors.miaosha.domain.User;
import com.juniors.miaosha.redis.RedisService;
import com.juniors.miaosha.redis.UserKey;
import com.juniors.miaosha.result.CodeMsg;
import com.juniors.miaosha.result.Result;
import com.juniors.miaosha.service.MiaoshaUserService;
import com.juniors.miaosha.service.UserService;
import com.juniors.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(path = "/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping(path = "/doLogin")
    @ResponseBody
    public Result<Boolean> doLogin(LoginVo loginVo){

        logger.info(loginVo.toString());
        //参数校验
        String passwdInput = loginVo.getPassword();
        String mobile = loginVo.getMobile();
        if(StringUtils.isEmpty(passwdInput)){
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }
        if (VaildatorUtil.isMobile(mobile)){
            return Result.error(CodeMsg.MOBILE_ERROR);
        }
        //登录
        CodeMsg loginMsg = miaoshaUserService.login(loginVo);
        if (loginMsg.getCode() == 0){
            return Result.success(true);

        }else {
            return Result.error(loginMsg);
        }
    }
}
