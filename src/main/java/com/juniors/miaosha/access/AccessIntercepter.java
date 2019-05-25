package com.juniors.miaosha.access;

import com.alibaba.fastjson.JSON;
import com.juniors.miaosha.domain.MiaoshaUser;
import com.juniors.miaosha.redis.AccessKey;
import com.juniors.miaosha.redis.RedisService;
import com.juniors.miaosha.result.CodeMsg;
import com.juniors.miaosha.result.Result;
import com.juniors.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author Juniors
 */
@Service
public class AccessIntercepter extends HandlerInterceptorAdapter {

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod){
            MiaoshaUser user = getUser(request,response);
            UserContext.setUser(user);

            HandlerMethod method = (HandlerMethod)handler;
            AccessLimit accessLimit = method.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null){
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if (needLogin){
                if (user == null){
                    render(response,CodeMsg.SESSION_ERROR);
                    return false;
                }
                key += "_" + user.getId();
            }else {
                // do nothing
            }
            AccessKey ak = AccessKey.withExpire(seconds);
            Integer count = redisService.get(ak,key,Integer.class);
            if (count == 0){
                redisService.set(ak,key,1);
            }else if (count < maxCount){
                redisService.incr(ak,key);
            }else {
                render(response,CodeMsg.ACCESS_LIMIT);
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, CodeMsg msg) throws Exception {
        response.setContentType("application/json;charset:utf-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(msg));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private MiaoshaUser getUser(HttpServletRequest webRequest, HttpServletResponse webResponse){
        String paramToken = webRequest.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookie(webRequest,MiaoshaUserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return null;
        }
        //paramToken 优先级大于 cookieToken
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return miaoshaUserService.getByToken(webResponse,token);
    }

    private String getCookie(HttpServletRequest webRequest, String cookieNameToken) {

        Cookie[] cookies = webRequest.getCookies();
        if (cookies == null || cookies.length <= 0){
            return null;
        }
        for(Cookie cookie:cookies){
            if (cookie.getName().equals(cookieNameToken)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
