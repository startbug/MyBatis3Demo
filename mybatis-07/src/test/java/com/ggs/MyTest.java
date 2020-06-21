package com.ggs;

import com.ggs.dao.StudentMapper;
import com.ggs.dao.TeacherMapper;
import com.ggs.pojo.Student;
import com.ggs.pojo.Teacher;
import com.ggs.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @author: Starbug
 * @date: 2020-06-20 23:57
 */
public class MyTest {

    @Test
    public void testGetTeachers() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        List<Teacher> teachers = mapper.getTeachers2();
        teachers.forEach(System.out::println);
    }

}
