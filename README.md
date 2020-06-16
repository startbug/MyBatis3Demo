# 1、简介

## 1.1、什么是Mybatis

![MyBatis logo](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200614173054.png)

- MyBatis 是一款优秀的**持久层框架**
- 它支持自定义 SQL、存储过程以及高级映射。
- MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。
- MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。
- MyBatis 本是[apache](https://baike.baidu.com/item/apache/6265)的一个开源项目[iBatis](https://baike.baidu.com/item/iBatis), 2010年这个项目由apache software foundation 迁移到了google code，并且改名为MyBatis 。
- 2013年11月迁移到Github。



如何获取Mybaits?

```xml
<!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.2</version>
</dependency>
```

- Github : https://github.com/mybatis/mybatis-3
- 中文文档 : https://mybatis.org/mybatis-3/zh/index.html

-------



## 1.2、持久化

数据持久化

- 持久化就是将程序的数据在持久状态和瞬时状态转化的过程
- 内存: 断电丢失
- 数据库(jdbc), IO文件持久化(耗资源)

为什么需要持久化?

	- 数据不能丢失
	- 内存贵

------



## 1.3、持久层

三层架构: Dao层、Service层、Controller层

- 完成持久化工作(操作)的代码块
- 界限十分明显

-----------



## 1.3、为什么需要Mybatis?

- 方便
- 传统的JDBC代码复杂. 框架简化操作, 自动化流程
- 框架操作容易上手
- 优点: 

> **简单易学**：本身就很小且简单。没有任何第三方依赖，最简单安装只要两个jar文件+配置几个sql映射文件易于学习，易于使用，通过文档和源代码，可以比较完全的掌握它的设计思路和实现。

> **灵活**：mybatis不会对应用程序或者数据库的现有设计强加任何影响。 sql写在xml里，便于统一管理和优化。通过sql语句可以满足操作数据库的所有需求。

> **解除sql与程序代码的耦合**：通过提供DAO层，将业务逻辑和数据访问逻辑分离，使系统的设计更清晰，更易维护，更易单元测试。sql和代码的分离，提高了可维护性。

> **提供映射标签，支持对象与数据库的orm字段关系映射**

> **提供对象关系映射标签，支持对象关系组建维护**

> **提供xml标签，支持编写动态sql。**

----------



# 2、第一个Mybatis程序

思路: 搭建环境-->导入Mybatis-->编写代码-->测试

## 	2.1、搭建环境

​	搭建数据库

```sql
create database `mybatis`;

use `mybatis`;

create table `user`(
	`id` int(20) not null,
	`name` varchar(30) default null,
	`pwd` varchar(30) default null,
	primary key(`id`)
)engine=INNODB default charset=utf8;

insert into `user` (`id`,`name`,`pwd`) values 
(1,'劳资','123456'),
(2,'zhangs','123445'),
(3,'lisi','123456')
```

​	新建项目, 导入依赖

```xml
<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.20</version>
    </dependency>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.2</version>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

​	

## 	2.2、创建一个maven模块

- 编写mybatis的核心配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?serverTimezone=GMT%2B8&amp;characterEncoding=UTF-8&amp;useSSL=true&amp;useUnicode=true"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="org/mybatis/example/BlogMapper.xml"/>
    </mappers>
</configuration>
```

- 编写mybatis工具类

```java
/**
 * @author: Starbug
 * @date: 2020/6/14 18:33 sqlSessionFactory --> sqlSession
 */
public class MybatisUtils {
  private static SqlSessionFactory sqlSessionFactory;

  static {
    try {
      String resource = "mybatis-config.xml"; // 类路径下
      InputStream inputStream = Resources.getResourceAsStream(resource);
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 既然有了 SqlSessionFactory，顾名思义，我们可以从中获得 SqlSession 的实例。
   * SqlSession 提供了在数据库执行 SQL 命令所需的所有方法。
   * 你可以通过 SqlSession 实例来直接执行已映射的 SQL 语句。
   * @return
   */
  public SqlSession getSqlSession(){
    return sqlSessionFactory.openSession();
  }
}
```

---------



## 2.3、编写代码

- 实体类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  private int id;
  private String name;
  private String pwd;
}
```

- Dao接口

```java
public interface UserDao {
    List<User> getAll();
}
```

- 接口实现类由原来的UserDaoImpl转变为一个Mapper配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace绑定一个对应的Dao/Mapper接口-->
<mapper namespace="com.ggs.dao.com.ggs.dao.UserMapper">
    <!--select查询语句-->
    <select id="getAll" resultType="com.ggs.pojo.User">
        select * from user
    </select>
</mapper>
```

## 2.4、测试

注意:org.apache.ibatis.binding.BindingException: Type interface com.ggs.dao.com.ggs.dao.UserMapper is not known to the MapperRegistry.

**MapperRegistry是什么?**

Mapper的注册中心

-----

解决maven项目,放在src/main/java包下的配置文件无法被编译到对应的文件夹的问题(也叫做maven导出资源问题)

```xml
<!--maven由于他的约定大于配置,我们之后可能遇到我们写的配置文件无法被到处或者生效的问题,解决方案-->
<build>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>true</filtering>
        </resource>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>true</filtering>
        </resource>
    </resources>
</build>
```

- Junit测试

```java
@Test
public void test1() {
    // 获取sqlSession对象
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    // 执行sql, 通过sqlSession获取UserDao接口的实现类,赋值给UserDao,多态的体现
    UserDao mapper = sqlSession.getMapper(UserDao.class);
    List<User> userList = mapper.getAll();
    System.out.println(userList);
    //关闭sqlSession
    sqlSession.close();
}
```

也可以使用下面的方式执行方法,但是不推荐

```java
@Test
public void test1() {
    // 获取sqlSession对象
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    List<User> userList = sqlSession.selectList("com.ggs.dao.com.ggs.dao.UserMapper.getAll");
    System.out.println(userList);
    //关闭sqlSession
    sqlSession.close();
}
```

可能会遇到的问题:

​	1.配置文件没有中蹙额

​	2.绑定接口错误(mapper文件的namespace命名空间指定错误)

​	3.方法名不对

​	4.返回类型不对

​	5.Maven导出资源问题

