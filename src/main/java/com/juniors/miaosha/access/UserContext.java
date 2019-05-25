package com.juniors.miaosha.access;

import com.juniors.miaosha.domain.MiaoshaUser;

/**
 * @author Juniors
 */
public class UserContext {

    private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<MiaoshaUser>();

    public static void setUser(MiaoshaUser user){
        userHolder.set(user);
    }

    public static MiaoshaUser getUser(){
        return userHolder.get();
    }
}
