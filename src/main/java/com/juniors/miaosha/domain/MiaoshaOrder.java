package com.juniors.miaosha.domain;

import lombok.Getter;
import lombok.Setter;

/**
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
