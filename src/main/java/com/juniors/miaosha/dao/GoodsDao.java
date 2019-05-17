package com.juniors.miaosha.dao;

import com.juniors.miaosha.domain.Goods;
import com.juniors.miaosha.domain.MiaoshaGoods;
import com.juniors.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 商品 Dao 操作类
 * @author Juniors
 */
@Mapper
public interface GoodsDao {

    /**
     * 查询商品列表类
     * @return
     */
    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from ms_goods mg left join goods g on mg.goods_id = g.id")
    List<GoodsVo> listGoodsVo();

    /**
     * 通过商品ID查询相应商品
     * @param goodsId
     * @return
     */
    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from ms_goods mg left join goods g on mg.goods_id = g.id where g.id=#{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    /**
     * 库存减一操作
     * @param g
     */
    @Update("update ms_goods set stock_count = stock_count - 1 where goods_id = #{goodsId}")
    void reduceStock(MiaoshaGoods g);
}