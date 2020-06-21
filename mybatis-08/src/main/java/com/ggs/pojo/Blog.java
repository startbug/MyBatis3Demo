package com.ggs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Starbug
 * @date: 2020-06-21 22:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
    private String id;
    private String title;
    private String author;
    private Date createTime;
    private int views;
}
