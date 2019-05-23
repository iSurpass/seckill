package com.juniors.miaosha.dao;

import com.juniors.miaosha.domain.MiaoshaOrder;
import com.juniors.miaosha.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

/**
 * 订单 Dao 操作类
 * @author Juniors
 */
@Mapper
public interface OrderDao {

    /**
     * 由用户id和商品id查询秒杀订单
     * @param userId
     * @param goodsId
     * @return
     */
    @Select("select * from ms_order where user_id = #{userId} and goods_id = #{goodsId}")
    MiaoshaOrder getMiaoshaOrderByUserIdAndGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    /**
     * order_info表插入操作
     * @param orderInfo
     * @return
     */
    @Insert("insert into order_info(user_id,goods_id,goods_name,goods_count,goods_price,order_channel,status,create_date)values(" +
            "#{userId},#{goodsId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
    @SelectKey(keyColumn = "id",keyProperty = "id",resultType = long.class,before = false,statement = "select last_insert_id()")
    long insert(OrderInfo orderInfo);

    /**
     * ms_order表插入操作
     * @param miaoshaOrder
     */
    @Insert("insert into ms_order (user_id,goods_id,order_id)values(#{userId},#{goodsId},#{orderId})")
    void insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    @Select("select * from order_info where id =#{id}")
    OrderInfo getOrderById(@Param("id") long id);
}
