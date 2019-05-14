package com.juniors.miaosha.redis;

/**
 * @author Juniors
 */
public interface KeyPrefix {

    public int getExpireSeconds();

    public String getPrefix();

}
