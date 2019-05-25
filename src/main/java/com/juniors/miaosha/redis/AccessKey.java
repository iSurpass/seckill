package com.juniors.miaosha.redis;

/**
 * @author Juniors
 */
public class AccessKey extends BasePrefix{

    public AccessKey(int exp, String prefix) {
        super(exp,prefix);
    }

    public static AccessKey withExpire(int expire){
        return new AccessKey(expire,"access");
    }
}
