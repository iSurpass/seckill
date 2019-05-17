package com.juniors.miaosha.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 秒杀商品列表
 * @author Juniors
 */
@Setter
@Getter
public class MiaoshaGoods {

    private Long id;

    private Long goodsId;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

}
