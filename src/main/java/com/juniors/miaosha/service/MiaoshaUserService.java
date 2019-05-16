package com.juniors.miaosha.service;

import com.juniors.miaosha.exception.GlobalException;
import com.juniors.miaosha.redis.MiaoshaUserKey;
import com.juniors.miaosha.redis.RedisService;
import com.juniors.miaosha.util.MD5Util;
import com.juniors.miaosha.dao.MiaoshaUserDao;
import com.juniors.miaosha.domain.MiaoshaUser;
import com.juniors.miaosha.result.CodeMsg;
import com.juniors.miaosha.util.UUIDUtil;
import com.juniors.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 秒杀项目---用户服务类
 * @author Juniors
 */
@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(long id){

        return miaoshaUserDao.getById(id);

    }


    public MiaoshaUser getByToken(String token){
        if (StringUtils.isEmpty(token)){
            return null;
        }
        return redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);
    }

    /**
     *
     * @param loginVo
     * @return
     */
    public boolean login(HttpServletResponse response, LoginVo loginVo) {

        if (loginVo == null){
            System.out.println("77777777777777777");
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String pwd = loginVo.getPassword();

        //判断手机号是否存在
        MiaoshaUser user = miaoshaUserDao.getById(Long.parseLong(mobile));
        if (user == null){
            throw new GlobalException(CodeMsg.USER_NOT_EXIST);
        }

        //验证密码
        String pwdDB = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPwdToDB(pwd,saltDB);
        if (!calcPass.equals(pwdDB)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        //生成token---->Redis
        String token = UUIDUtil.uuid();
        //
        redisService.set(MiaoshaUserKey.token,token,user);

        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.getExpireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);

        return true;
    }
}
