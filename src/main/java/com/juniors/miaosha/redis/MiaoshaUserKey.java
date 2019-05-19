package com.juniors.miaosha.redis;

/**
 * @author Juniors
 */
public class MiaoshaUserKey extends BasePrefix{

    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;

    public MiaoshaUserKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE,"tk");

    //对象级缓存 有效时间永久
    public static MiaoshaUserKey getById = new MiaoshaUserKey(0,"id");

}
