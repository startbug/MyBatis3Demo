package com.ggs.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author: Starbug
 * @date: 2020/6/14 18:33 sqlSessionFactory --> sqlSession
 */
public class MybatisUtils {
  private static SqlSessionFactory sqlSessionFactory;

  static {
    try {
      String resource = "mybatis-config.xml"; // 类路径下
      InputStream inputStream = Resources.getResourceAsStream(resource);
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 既然有了 SqlSessionFactory，顾名思义，我们可以从中获得 SqlSession 的实例。
   * SqlSession 提供了在数据库执行 SQL 命令所需的所有方法。
   * 你可以通过 SqlSession 实例来直接执行已映射的 SQL 语句。
   * @return
   */
  public static SqlSession getSqlSession(){
    return sqlSessionFactory.openSession();
  }
}
