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

    List<User> getUserLike(String name);

    //分页1
    List<User> getUserByLimit(Map<String,Integer> map);

    //分页2
    List<User> getUserByRowBounds();
}
