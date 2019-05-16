package com.juniors.miaosha.redis;

/**
 * @author Juniors
 */
public interface KeyPrefix {

    public String getPrefix();

    public int getExpireSeconds();

}
