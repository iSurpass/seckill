package com.juniors.miaosha.redis;

/**
 * @author Juniors
 */
public class GoodsKey extends BasePrefix{

    public GoodsKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    //缓存商品列表页有效时间 60s
    public static GoodsKey getGoodsList = new GoodsKey(60,"gl");
    //缓存商品详情页有效时间 60s
    public static GoodsKey getGoodsDetail = new GoodsKey(60,"gd");
}
