package com.juniors.miaosha.vo;

import com.juniors.miaosha.domain.Goods;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Juniors
 */
@Setter
@Getter
public class GoodsVo extends Goods {

    private Double miaoshaPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

}
