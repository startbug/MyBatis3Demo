package com.ggs.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author: Starbug
 * @date: 2020-06-20 23:52
 */
@Data
public class Teacher {
    private int id;
    private String name;
    private List<Student> students;
}
