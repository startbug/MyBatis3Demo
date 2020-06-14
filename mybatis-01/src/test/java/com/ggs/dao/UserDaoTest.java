package com.ggs.dao;

import com.ggs.pojo.User;
import com.ggs.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @author: Starbug
 * @date: 2020/6/14 19:29
 */
public class UserDaoTest {

  @Test
  public void test1() {
    // 获取sqlSession对象
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    try {
      // 执行sql, 通过sqlSession获取UserDao接口的实现类,赋值给UserDao,多态的体现
      UserDao mapper = sqlSession.getMapper(UserDao.class);
      List<User> userList = mapper.getAll();
      // List<User> userList = sqlSession.selectList("com.ggs.dao.UserDao.getAll");
      System.out.println(userList);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 关闭sqlSession
      sqlSession.close();
    }
  }
}
