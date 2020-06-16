package com.ggs.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author: Starbug
 * @date: 2020/6/16 10:13
 */
public class MyBatisUtils {
  private static SqlSessionFactory sqlSessionFactory;

  static {
    try {
      String source = "mybatis-config.xml";
      InputStream resourceAsStream = Resources.getResourceAsStream(source);
      SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
      sqlSessionFactory = sqlSessionFactoryBuilder.build(resourceAsStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static SqlSession getSqlSession() {
    return sqlSessionFactory.openSession();
  }
}
