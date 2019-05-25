package com.juniors.miaosha.redis;

/**
 * @author Juniors
 */
public class MiaoshaKey extends BasePrefix{

    public MiaoshaKey(String prefix) {
        super(prefix);
    }

    public static MiaoshaKey isGoodsOver = new MiaoshaKey("go");
}
