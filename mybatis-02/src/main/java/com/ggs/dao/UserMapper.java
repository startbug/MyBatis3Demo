package com.ggs.dao;

import com.ggs.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * @author: Starbug
 * @date: 2020/6/14 19:09
 */
public interface UserMapper {
    List<User> getAll();

    User getById(int id);


    int insertUser(User user);

    int updateUser(User user);

    int deleteUser(int id);

    int updateUser2(Map<String,Object> map);

    List<User> getUserLike(String name);
}
