package com.ggs.dao;

import com.ggs.pojo.Blog;

import java.util.List;
import java.util.Map;

/**
 * @author: Starbug
 * @date: 2020-06-21 22:18
 */
public interface BlogMapper {

    int addBlog(Blog blog);

    //查询博客
    List<Blog> queryBlogIf(Map map);

}
