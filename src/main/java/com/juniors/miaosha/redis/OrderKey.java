package com.juniors.miaosha.redis;

/**
 * @author Juniors
 */
public class OrderKey extends BasePrefix{

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    @Override
    public int getExpireSeconds() {
        return 0;
    }
}
