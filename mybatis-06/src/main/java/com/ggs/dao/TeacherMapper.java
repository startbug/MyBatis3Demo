package com.ggs.dao;

import com.ggs.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author: Starbug
 * @date: 2020-06-20 23:54
 */
public interface TeacherMapper {

    @Select("select * from teacher where id = #{tid}")
    Teacher getTeacher(@Param("tid") int id);

}
