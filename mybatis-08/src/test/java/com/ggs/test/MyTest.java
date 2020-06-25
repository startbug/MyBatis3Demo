package com.ggs.test;

import com.ggs.dao.BlogMapper;
import com.ggs.pojo.Blog;
import com.ggs.utils.MybatisUtils;
import com.ggs.utils.ToolKit;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author: Starbug
 * @date: 2020-06-21 22:26
 */
public class MyTest {

    @Test
    public void insertBlogTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        mapper.addBlog(new Blog(ToolKit.getId(), "Starbug博客", "starbug", new Date(), 10));
        mapper.addBlog(new Blog(ToolKit.getId(), "Mybatis博客", "starbug", new Date(), 10));
        mapper.addBlog(new Blog(ToolKit.getId(), "微服务博客", "starbug", new Date(), 10));
        mapper.addBlog(new Blog(ToolKit.getId(), "Spring博客", "starbug", new Date(), 10));
    }

    @Test
    public void testQuery() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Map<String, String> map = new HashMap<>();
        map.put("title", "S");
        map.put("author", "starbugc");
        List<Blog> blogs = mapper.queryBlogIf(map);
        blogs.forEach(System.out::println);
    }


    @Test
    public void testQueryChoose() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Map<String, String> map = new HashMap<>();
//        map.put("title","S");
        map.put("views", "10");
//        map.put("author","starbugc");
        List<Blog> blogs = mapper.queryBlogChoose(map);
        blogs.forEach(System.out::println);
    }

    @Test
    public void testQueryBlogForeach(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Map<String, Object> map = new HashMap<>();
        List ids=new ArrayList<>();
        map.put("ids",ids);
        ids.add("3114f6a3b0894fbaa592f69cf6e93c4c");
        ids.add("50a43483f3f0446b8b6db375c939858d");
        ids.add("5a783c8d94664f20a77cd49bef8c61bf");
        List<Blog> blogs = mapper.queryBlogForeach(map);
        blogs.forEach(System.out::println);
    }



    @Test
    public void testUpdateBlog() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Map<String, String> map = new HashMap<>();
        map.put("title", "单身");
        map.put("views", "999");
        map.put("author", "星星虫");
        map.put("id", "8c5548ac017347fe8ee20452c051289f");
        int effectRow = mapper.updateBlog(map);
        System.out.println(effectRow);
    }
}
