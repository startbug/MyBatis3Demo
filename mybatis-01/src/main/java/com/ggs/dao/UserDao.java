package com.ggs.dao;

import com.ggs.pojo.User;

import java.util.List;

/**
 * @author: Starbug
 * @date: 2020/6/14 19:09
 */
public interface UserDao {
    List<User> getAll();
}
