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

    List<Blog> queryBlogChoose(Map map);

    //更新博客
    int updateBlog(Map map);

    //查询第1-3号记录的博客
    List<Blog> queryBlogForeach(Map map);
}
