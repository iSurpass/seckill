package com.juniors.miaosha.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 秒杀订单类
 * @author Juniors
 */
@Setter
@Getter
public class MiaoshaOrder {

    private Long id;

    private Long userId;

    private Long orderId;

    private Long goodsId;
}
