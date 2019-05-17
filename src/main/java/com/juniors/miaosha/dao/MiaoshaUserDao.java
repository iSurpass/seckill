package com.juniors.miaosha.dao;

import com.juniors.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 秒杀用户 Dao 操作类
 * @author Juniors
 */
@Mapper
public interface MiaoshaUserDao {

    /**
     * 以 id 主键查询相应用户
     * @param id
     * @return
     */
    @Select("select * from ms_user where id = #{id}")
    MiaoshaUser getById(@Param("id") long id);

}
