package com.ggs.utils;


import java.util.UUID;

/**
 * @author: Starbug
 * @date: 2020-06-21 22:21
 */
public class ToolKit {
  public static String getId() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
