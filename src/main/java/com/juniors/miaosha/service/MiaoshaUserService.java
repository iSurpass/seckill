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

    /**
     * 对象级缓存实例
     * @param id
     * @return
     */
    public MiaoshaUser getById(long id){

        //去缓存里取
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById,""+id,MiaoshaUser.class);
        if (user != null){
            return user;
        }
        //如果redis缓存中没有用户数据则向数据库里取并加载到redis中
        user =  miaoshaUserDao.getById(id);
        if (user != null){
            redisService.set(MiaoshaUserKey.getById,""+id,user);
        }
        return user;
    }

    /**
     *
     * @param token
     * @param id
     * @param formPassword
     * @return
     */
    public boolean updatePassword(String token,long id,String formPassword){

        MiaoshaUser user = getById(id);
        if (user == null){
            throw new GlobalException(CodeMsg.USER_NOT_EXIST);
        }
        //更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPwdToDB(formPassword,user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);
        //****更新处理redis缓存*******
        redisService.delete(MiaoshaUserKey.getById,""+id);
        //****token不能Delete而是重新set，否则造成数据不一致的严重后果
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token,token,user);
        return true;
    }


    /**
     *
     * @param response
     * @param token
     * @return
     */
    public MiaoshaUser getByToken(HttpServletResponse response,String token){
        if (StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);
        //更新Cookie以延长有效期
        if (user != null){
            addCookie(response,token,user);
        }
        return user;
    }


    /**
     *
     * @param response
     * @param token
     * @param user
     */
    private void addCookie(HttpServletResponse response,String token,MiaoshaUser user){
        //
        redisService.set(MiaoshaUserKey.token,token,user);

        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.getExpireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    /**
     *
     * @param loginVo
     * @return
     */
    public boolean login(HttpServletResponse response, LoginVo loginVo) {

        if (loginVo == null){
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
        addCookie(response,token,user);

        return true;
    }
}
