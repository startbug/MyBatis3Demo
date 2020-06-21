package com.ggs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Starbug
 * @date: 2020-06-19 23:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class User {

    private int id;
    private String name;
    private String pwd;


}
