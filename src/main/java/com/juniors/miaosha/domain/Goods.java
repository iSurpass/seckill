package com.juniors.miaosha.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Juniors
 */
@Setter
@Getter
public class Goods {

    private Long id;

    private String goodsName;

    private String goodsTitle;

    private String goodsImg;

    private String goodsDetail;

    private Double goodsPrice;

    private Integer goodsStock;

}
