package com.ggs.test;

import com.ggs.dao.UserMapper;
import com.ggs.pojo.User;
import com.ggs.utils.MybatisUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @author: Starbug
 * @date: 2020-06-19 23:13
 */
public class UserMapperTest {

    /**
     * sqlSession.getMapper(**.class)
     * 最终通过jdk动态代理生成代理对象
     * 可以在org.apache.ibatis.binding.MapperProxyFactory中的newInstance方法看到执行过程
     */
    @Test
    public void test1() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = mapper.getUsers();
        users.forEach(System.out::println);
        sqlSession.close();
    }

    @Test
    public void testInsertUser() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.insertUser(new User(5, "lucy", "4156135413"));
    }

    @Test
    public void testUpdateUser() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.updateUser(new User(5, "xxx", "skldfjklsdjf"));
    }

    @Test
    public void testDeleteUser() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.deleteUser(5);
    }
}
