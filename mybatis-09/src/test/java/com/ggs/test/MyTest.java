package com.ggs.test;

import com.ggs.dao.UserMapper;
import com.ggs.pojo.User;
import com.ggs.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Starbug
 * @date: 2020-06-25 17:10
 */
public class MyTest {

  @Test
  public void test1() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    User user1 = mapper.queryUserById(2);
    System.out.println(user1);
    System.out.println("------------------------------------");
    //    mapper.updateUser(new User(3,"mmp","321654"));
    sqlSession.clearCache();
    System.out.println("------------------------------------");
    User user2 = mapper.queryUserById(2);
    System.out.println(user2);
    sqlSession.close();
  }

  @Test
  public void test2() {
    SqlSession sqlSession1 = MybatisUtils.getSqlSession();
    UserMapper mapper1 = sqlSession1.getMapper(UserMapper.class);
    SqlSession sqlSession2 = MybatisUtils.getSqlSession();
    UserMapper mapper2 = sqlSession2.getMapper(UserMapper.class);

    User user1 = mapper1.queryUserById(2);
    System.out.println(user1);
    System.out.println("-------------------1-------------------------");
    User user1_1 = mapper1.queryUserById(2);
    System.out.println(user1_1);
    sqlSession1.close();

    User user2 = mapper2.queryUserById(2);
    System.out.println(user2);

    System.out.println("-------------------2-------------------------");
    User user3 = mapper2.queryUserById(3);
    System.out.println(user3);

    System.out.println("-------------------3-------------------------");
    User user3_1 = mapper2.queryUserById(3);
    System.out.println(user3_1);

    sqlSession2.close();
    System.out.println(user3 == user3_1);
    System.out.println(user1 == user2);
  }
}
