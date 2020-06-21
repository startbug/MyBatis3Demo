package com.ggs.pojo;

import lombok.Data;

/**
 * @author: Starbug
 * @date: 2020-06-20 23:52
 */
@Data
public class Student {
    private int id;
    private String name;
    //多对一,关联一个老师对象
    private int tid;
}
