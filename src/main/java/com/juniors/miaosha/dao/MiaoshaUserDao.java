package com.juniors.miaosha.dao;

import com.juniors.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Juniors
 */
@Mapper
public interface MiaoshaUserDao {

    @Select("select * from ms_user where id = #{id}")
    MiaoshaUser getById(@Param("id") long id);


}
