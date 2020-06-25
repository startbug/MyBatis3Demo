package com.ggs.dao;

import com.ggs.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *  @author: Starbug
 *  @date: 2020-06-25 17:08
 */
public interface UserMapper {

    User queryUserById(@Param("id") int id);

    int updateUser(User user);
}
