package com.juniors.miaosha.vo;

import com.juniors.miaosha.domain.OrderInfo;

import java.io.Serializable;

/**
 * @author Juniors
 */
public class OrderDetailVo implements Serializable {

    private GoodsVo goods;

    private OrderInfo order;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }
}
