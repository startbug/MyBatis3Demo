package com.ggs.dao;

import com.ggs.pojo.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author: Starbug
 * @date: 2020-06-19 23:07
 */
public interface UserMapper {

    @Select("select * from user")
    List<User> getUsers();

    @Insert("insert into user(id,name,pwd) values(#{id},#{name},#{pwd})")
    int insertUser(User user);

    @Update("update user set name=#{name},pwd=#{pwd} where id=#{id}")
    int updateUser(User user);

    @Delete("delete from user where id =#{id}")
    int deleteUser(int id);
}

