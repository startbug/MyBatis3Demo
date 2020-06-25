package com.ggs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: Starbug
 * @date: 2020-06-25 17:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  private int id;
  private String name;
  private String pwd;
}
