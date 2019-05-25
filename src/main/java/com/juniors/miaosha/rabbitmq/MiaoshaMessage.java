package com.juniors.miaosha.rabbitmq;

import com.juniors.miaosha.domain.MiaoshaUser;

/**
 * @author Juniors
 */
public class MiaoshaMessage {

    private MiaoshaUser user;

    private long goodsId;

    public MiaoshaMessage(MiaoshaUser user, long goodsId) {
        this.user = user;
        this.goodsId = goodsId;
    }

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
