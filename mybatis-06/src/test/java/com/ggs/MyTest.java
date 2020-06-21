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
    public void getTeacherTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacher = mapper.getTeacher(1);

        System.out.println(teacher);
    }

    @Test
    public void getAllStudentTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> studentList = mapper.getAllStudent();
        studentList.forEach(System.out::println);
    }

    @Test
    public void getAllStudent2Test(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> studentList = mapper.getAllStudent2();
        studentList.forEach(System.out::println);
    }

}
