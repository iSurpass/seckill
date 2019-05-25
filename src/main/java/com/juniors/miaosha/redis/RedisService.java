package com.juniors.miaosha.redis;

import com.alibaba.fastjson.JSON;
import com.juniors.miaosha.redis.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Juniors
 */
@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    /**
     * 获取单个redis对象
     * @param prefix
     * @param key
     * @param clazz
     * @param <T>
     * @return -----泛型------
     */
    public <T> T get(KeyPrefix prefix,String key,Class<T> clazz){
        Jedis jedis = null;
        //连接池记得及时返回
        try {
            jedis = jedisPool.getResource();
            //生成真正的Key
            String realKey = prefix.getPrefix() + key;
            String str = jedis.get(realKey);
            T t = stringToBean(str,clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置redis对象
     * @param prefix
     * @param key
     * @param value
     * @param <T>
     * @return 判断创建对象(key-value)是否成功
     */
    public <T> boolean set(KeyPrefix prefix,String key,T value){
        Jedis jedis = null;
        //连接池记得及时返回
        try {
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if (str == null || str.length() <= 0){
                return false;
            }
            //生成真正的Key
            String realKey = prefix.getPrefix() + key;
            int seconds = prefix.getExpireSeconds();
            if (seconds <= 0){
                //说明永久保存
                jedis.set(realKey,str);
            }else {
                jedis.setex(realKey,seconds,str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断redis对象是否存在
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean exists(KeyPrefix prefix,String key){
        Jedis jedis = null;
        //连接池记得及时返回
        try {
            jedis = jedisPool.getResource();
            //生成真正的Key
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 删除redis对象
     * @param prefix
     * @param key
     * @return
     */
    public boolean delete(KeyPrefix prefix,String key){
        Jedis jedis = null;
        //连接池记得及时返回
        try {
            jedis = jedisPool.getResource();
            //生成真正的Key
            String realKey = prefix.getPrefix() + key;
            long ret = jedis.del(key);
            return ret > 0;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 对redis对象增加值
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix prefix,String key){
        Jedis jedis = null;
        //连接池记得及时返回
        try {
            jedis = jedisPool.getResource();
            //生成真正的Key
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 对redis对象减值
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix prefix,String key){
        Jedis jedis = null;
        //连接池记得及时返回
        try {
            jedis = jedisPool.getResource();
            //生成真正的Key
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 由 Bean 对象转换为 String
     * @param value
     * @param <T>
     * @return
     */
        public static <T> String beanToString(T value) {
        if (value == null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class){
            return value.toString();
        }else if (clazz == String.class){
            return (String)value;
        }else if (clazz == long.class || clazz == Long.class){
            return value.toString();
        }else {
            return JSON.toJSONString(value);
        }

    }

    /**
     * 由于 String 转换为 Bean 对象
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T stringToBean(String str,Class<T> clazz) {

        if (str == null || str.length() <= 0 || clazz == null){
            return null;
        }
        if (clazz == int.class || clazz == Integer.class){
            return (T) Integer.valueOf(str);
        }else if (clazz == String.class){
            return (T) str;
        }else if (clazz == long.class || clazz == Long.class){
            return (T) Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    /**
     * 不用redis池及时返回close
     * @param jedis
     */
    private void returnToPool(Jedis jedis) {
        if (jedis != null){
            jedis.close();
        }
    }

}
