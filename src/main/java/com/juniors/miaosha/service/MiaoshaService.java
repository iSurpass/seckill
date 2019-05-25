package com.juniors.miaosha.service;

import com.juniors.miaosha.dao.GoodsDao;
import com.juniors.miaosha.domain.Goods;
import com.juniors.miaosha.domain.MiaoshaOrder;
import com.juniors.miaosha.domain.MiaoshaUser;
import com.juniors.miaosha.domain.OrderInfo;
import com.juniors.miaosha.redis.MiaoshaKey;
import com.juniors.miaosha.redis.RedisService;
import com.juniors.miaosha.util.MD5Util;
import com.juniors.miaosha.util.UUIDUtil;
import com.juniors.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author Juniors
 */
@Service
public class MiaoshaService {

    //如果需要引入不是本类的 Dao 时，不应该直接引入，应该引入其 Service，进行间接引入 ------方便管理
    //@Autowired
    //GoodsDao goodsDao;
    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    private static char[] ops = new char[] {'+','-','*'};

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        if (success){
            //order_info miaosha_order
            return orderService.createOrder(user,goods);
        }else {
            setGoodsOver(goods.getId());
            return null;
        }
    }


    public long getMiaoshaResult(Long userId, long goodsId) {

        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(userId,goodsId);
        if (order != null){
            return order.getId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver){
                return -1;
            }else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long id) {
        redisService.set(MiaoshaKey.isGoodsOver,""+id,true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver,""+goodsId);
    }

    public boolean checkPath(MiaoshaUser user, long goodsId, String path) {

        if (user == null || path == null){
            return false;
        }
        String prePath = redisService.get(MiaoshaKey.getMiaoshaPath,""+user.getId()+"_"+goodsId,String.class);
        return path.equals(prePath);
    }

    public String createPath(MiaoshaUser user, long goodsId) {

        //生成随机串返回并存入Redis缓存中
        String str = MD5Util.md5(UUIDUtil.uuid() + "12345");
        redisService.set(MiaoshaKey.getMiaoshaPath,""+user.getId()+"_"+goodsId,str);

        return str;
    }

    public BufferedImage createVerifyCode(MiaoshaUser user, long goodsId) {

        if (user == null || goodsId <= 0){
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0,0,width-1,height-1);
        //在图片上随机生成小点点
        Random random = new Random();
        for (int i = 0;i<50;i++){
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g.drawOval(x,y,0,0);
        }
        String verifyCode = generateVerifyCode(random);
        g.setColor(new Color(0,100,0));
        g.setFont(new Font("Candara",Font.BOLD,24));
        g.drawString(verifyCode,8,24);
        g.dispose();
        //把验证码存到Redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode,user.getId()+","+goodsId,rnd);
        //输出图片
        return image;

    }

    private int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        }catch (Exception e){
            return 0;
        }
    }

    private String generateVerifyCode(Random random) {

        int num1 = random.nextInt(10);
        int num2 = random.nextInt(10);
        int num3 = random.nextInt(10);
        char op1 = ops[random.nextInt(3)];
        char op2 = ops[random.nextInt(3)];

        String exp = num1 + op1 + num2 + op2 + num3 + "";
        return null;
    }

    public boolean checkVerifyCode(MiaoshaUser user, long goodsId, int verifyCode) {

        if (user == null || goodsId <= 0){
            return false;
        }
        int preVerifyCode = redisService.get(MiaoshaKey.getMiaoshaVerifyCode,user.getId()+","+goodsId,Integer.class);
        if (preVerifyCode <= 0 || preVerifyCode - verifyCode != 0){
            return false;
        }
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode,user.getId()+","+goodsId);
        return true;
    }
}
