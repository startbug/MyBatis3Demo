package com.ggs.test;

import com.ggs.dao.BlogMapper;
import com.ggs.pojo.Blog;
import com.ggs.utils.MybatisUtils;
import com.ggs.utils.ToolKit;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Starbug
 * @date: 2020-06-21 22:26
 */
public class MyTest {

    @Test
    public void insertBlogTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        mapper.addBlog(new Blog(ToolKit.getId(),"Starbug博客","starbug",new Date(),10));
        mapper.addBlog(new Blog(ToolKit.getId(),"Mybatis博客","starbug",new Date(),10));
        mapper.addBlog(new Blog(ToolKit.getId(),"微服务博客","starbug",new Date(),10));
        mapper.addBlog(new Blog(ToolKit.getId(),"Spring博客","starbug",new Date(),10));
    }

    @Test
    public void testQuery(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Map<String, String> map = new HashMap<>();
        map.put("title","S");
        map.put("author","starbugc");
        List<Blog> blogs = mapper.queryBlogIf(map);
        blogs.forEach(System.out::println);
    }
}
