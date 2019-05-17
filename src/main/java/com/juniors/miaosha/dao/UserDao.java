package com.juniors.miaosha.dao;

import com.juniors.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * UserDao类数据库事务封装
 * @author Juniors
 */
@Mapper
public interface UserDao {

    /**
     * 以id为主键查询相应用户
     * @param id
     * @return
     */
    @Select("select * from ms_user where id = #{id}")
    public User getById(@Param("id") int id);

    @Insert("insert into ms_user(id,name) values(#{id},#{name})")
    public int insertTest(User user);
}
