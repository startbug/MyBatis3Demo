package com.ggs.dao;

import com.ggs.pojo.User;
import com.ggs.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Starbug
 * @date: 2020/6/14 19:29
 */
public class UserDaoTest {

    @Test
    public void getAllTest() {
        // 获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        try {
            // 执行sql, 通过sqlSession获取UserDao接口的实现类,赋值给UserDao,多态的体现
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
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

    //-- select id,pwd password,name username from user where id=#{id}
    @Test
    public void getByIdTest() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        try {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = mapper.getById(2);
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void insertUserTest() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MyBatisUtils.getSqlSession();
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            int count = mapper.insertUser(new User(10, "tom", "123456"));
            System.out.println(count);
            sqlSession.commit(); // 提交事务
            if (count > 0) {
                System.out.println("插入成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void getUserLike() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        try {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);

            List<User> userList = mapper.getUserLike("l");
            userList.forEach(user -> System.out.println(user));
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    static Logger logger = Logger.getLogger(UserDaoTest.class);

    @Test
    public void testLog4j() {
        logger.trace("trace级别日志");
        logger.info("info级别日志");
        logger.debug("debug级别日志");
        logger.warn("warn级别日志");
        logger.error("error级别日志");
    }

}
