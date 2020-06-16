package com.ggs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Starbug
 * @date: 2020/6/14 19:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//给JavaBean起别名,在mapper.xml文件中引用
public class User {
  private int id;
  private String username;
  private String password;
}
