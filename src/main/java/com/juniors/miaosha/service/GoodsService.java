package com.juniors.miaosha.service;

import com.juniors.miaosha.dao.GoodsDao;
import com.juniors.miaosha.domain.Goods;
import com.juniors.miaosha.domain.MiaoshaGoods;
import com.juniors.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Juniors
 */
@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> lisGoodsVo(){

        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public void reduceStock(GoodsVo goods) {

        MiaoshaGoods g = new MiaoshaGoods();
        g.setId(goods.getId());
        goodsDao.reduceStock(g);
    }
}
