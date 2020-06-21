package com.ggs.dao;

import com.ggs.pojo.Student;
import com.ggs.pojo.Teacher;

import java.util.List;

/**
 * @author: Starbug
 * @date: 2020-06-20 23:54
 */
public interface StudentMapper {

    Teacher getTeacher(String id);

    List<Student> getAllStudent();

    List<Student> getAllStudent2();

}
