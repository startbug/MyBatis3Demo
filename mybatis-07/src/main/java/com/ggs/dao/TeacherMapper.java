package com.ggs.dao;

import com.ggs.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: Starbug
 * @date: 2020-06-20 23:54
 */
public interface TeacherMapper {

    List<Teacher> getTeachers();

    List<Teacher> getTeachers2();

}
