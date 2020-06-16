# 1.简介

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



# 2.第一个Mybatis程序

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
<mapper namespace="com.ggs.dao.UserDao">
    <!--select查询语句-->
    <select id="getAll" resultType="com.ggs.pojo.User">
        select * from user
    </select>
</mapper>
```

## 2.4、测试

注意:org.apache.ibatis.binding.BindingException: Type interface com.ggs.dao.UserDao is not known to the MapperRegistry.

**MapperRegistry是什么?**

Mapper的注册中心

-----

解决maven项目,放在src/main/java包下的配置文件无法被编译到对应的文件夹的问题(也叫做maven导出资源问题)

==Tips: 改了还不行,重启idea试试==

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
    List<User> userList = sqlSession.selectList("com.ggs.dao.UserDao.getAll");
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

-----



# 3.CRUD

## 1、**namespace**

​	namespace中的名字要和Dao/Mapper接口的全限定名(com.ggs.pojo.User)一致

## **2、select**

选择, 查询语句

- id: 就是对应namespace接口中的方法名
- resultType: sql语句执行后的返回值类型
- parameterType: 参数类型

1.编写接口

```java
//根据id查询用户信息
User getById(int id);
```

2.编写对应的mapper中的sql语句

```xml
<select id="getAll" resultType="com.ggs.pojo.User">
    select * from user
</select>
```

3.测试案例

```java
@Test
public void getAllTest() {
    // 获取sqlSession对象
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    try {
        // 执行sql, 通过sqlSession获取UserDao接口的实现类,赋值给UserDao,多态的体现
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = mapper.getAll();
        // List<User> userList = sqlSession.selectList("com.ggs.dao.UserDao.getAll");
        System.out.println(userList);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        // 关闭sqlSession
        sqlSession.close();
    }
}
```



## **3、insert**

1.编写接口

```java
//插入用户信息
int insertUser(User user);
```

2.编写对应的mapper中的sql语句

```xml
<insert id="insertUser" parameterType="com.ggs.pojo.User">
    insert into user(id,name,pwd) values(#{id},#{name},#{pwd});
</insert>
```

3.测试

```java
@Test
public void insertUserTest() {
    SqlSession sqlSession = null;
    try {
        sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int count = mapper.insertUser(new User(10, "tom", "123456"));
        System.out.println(count);
        sqlSession.commit(); // 提交事务
        if (count > 0) {
            System.out.println("插入成功");
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        sqlSession.close();
    }
}
```



## **4、update修改用户信息**

```xml
<update id="updateUser" parameterType="com.ggs.pojo.User">
    update mybatis.user set name=#{name},pwd=#{pwd} where id=#{id}
</update>
```



## **5、delete删除用户信息**

```xml
<delete id="deleteUser" parameterType="int">
    delete from mybatis.user where id=#{id}
</delete>
```



## 6、万能Map

字段key可以随便取

```java
//入参Map
int updateUser2(Map<String,Object> map);
```

#{}中根据map中的key取值即可

```xml
<update id="updateUser2" parameterType="map">
    update user set name=#{xxx} where id=#{myid}
</update>
```

好处: 修改数据时,可以不用传入整个对象, 只要把需要修改的值写入map即可,多于字段可以忽略不管

```java
@Test
public void updateUser2Test(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    try{
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        Map<String,Object> map=new HashMap<>();
        map.put("xxx","lucy");
        map.put("myid",10);

        mapper.updateUser2(map);
        sqlSession.commit();
    }catch (Exception e){
        e.printStackTrace();
    }finally{
        sqlSession.close();
    }
}
```

小结: 

Map传递参数: 直接从在sql中取出key即可

对象传递参数: 直接在sql中取出对象的属性即可

只有要给基本类型参数的情况下,可以直接在sql中取到

多个参数用Map, ==或者注解==



注意: 增删改都需要提交事务



## 7、模糊查询

1.代码执行的时候, 传递通配符%

```javascript
List<User> userList = mapper.getUserLike("%张%");
```

2.在sql中拼接试用通配符

```xml
<select id="getUserLike" resultType="com.ggs.pojo.User">
    select * from user where name like "%"#{name}"%"
</select>
```



--------

# 4.配置解析

## 1、核心配置文件

- mybatis-config.xml

- MyBatis的配置文件包含Mybatis行为的设置和属性信息

  configuration（配置）

```text
properties（属性）
settings（设置）
typeAliases（类型别名）
typeHandlers（类型处理器）
objectFactory（对象工厂）
plugins（插件）
environments（环境配置）
environment（环境变量）
transactionManager（事务管理器）
dataSource（数据源）
databaseIdProvider（数据库厂商标识）
mappers（映射器）
```



## 2、环境配置（environments）

Mybatis可以配置成适应多环境

但是,尽管可以配置多环境,但是每个SqlSessionFactory实例只能选择一种环境

Mybatis默认的事务管理器就是JDBC; 连接池: POOLED



## 3、属性(Properties)

我们可以通过properties属性来实现引用配置和文件

这些属性可以在外部进行配置，并可以进行动态替换。你既可以在典型的 Java 属性文件中配置这些属性，也可以在 properties 元素的子元素中设置。[db.properties]

编写配置文件(db.properties)

```properties
star_driver=com.mysql.cj.jdbc.Driver
star_url=jdbc:mysql://localhost:3306/mybatis?serverTimezone=GMT%2B8&amp;characterEncoding=UTF-8&amp;useSSL=true&amp;useUnicode=true
star_username=root
star_password=123456
```

在核心配置文件(mybatis-config.xml)中引用外部配置文件

```xml
<properties resource="db.properties"/>
```

- 可以直接引入外部文件
- 可以在其中增加一些属性配置
- 如果两个配置有同一个字段, 优先使用外部配置文件的



-----------

## 4、类型别名(typeAliases)

- 类型别名是为java类型设置一个短的名字
- 存在的意义仅在于用来减少类完全限定名的冗余

```xml
<typeAliases>
    <!--给实体类起别名-->
    <typeAlias type="com.ggs.pojo.User" alias="User"/>
</typeAliases>
```

也可以指定一个包名, Mybatis会把包名下面搜索需要的JavaBean,比如:

扫描实体类的包, 默认别名是这个类的类名(首字母小写)

官方解释: ==在没有注解的情况下，会使用 Bean 的首字母小写的非限定类名来作为它的别名==

```xml
<typeAliases>
    <!--指定包路径,Mybatis自动给该包下的javabean定义别名-->
    <package name="com.ggs.pojo"/>
</typeAliases>
```



第一种方式,直接指定JavaBean的全限定名,用的比较少,实体类少的时候适用

第二种方式, 指定包名, 实体类多的时候适用



第一种方式可以手动指定别名

第二种方式在配置文件中指定, 但是可以在JavaBean上使用注解进行指定

```java
//给JavaBean起别名,在mapper.xml文件中引用
@Alias(value = "hello")
public class User {
    private int id;
    private String name;
    private String pwd;
}
```

----





## 5、设置（settings）

这是 MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为

常用的两个:

日志默认没有设置具体实现, 驼峰命名规则映射默认为false(不开启)

![image-20200616122208995](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200616122214.png)

![image-20200616122313664](C:%5CUsers%5CStarbug%5CAppData%5CRoaming%5CTypora%5Ctypora-user-images%5Cimage-20200616122313664.png)



重要的两个: cacheEnabled是开启缓存功能, lazyLoadingEnabled是开启懒加载

作用: 提高效率

![image-20200616122439339](C:%5CUsers%5CStarbug%5CAppData%5CRoaming%5CTypora%5Ctypora-user-images%5Cimage-20200616122439339.png)



## 6、其他配置

- [typeHandlers（类型处理器）](https://mybatis.org/mybatis-3/zh/configuration.html#typeHandlers)

- [objectFactory（对象工厂）](https://mybatis.org/mybatis-3/zh/configuration.html#objectFactory)

- [plugins（插件）](https://mybatis.org/mybatis-3/zh/configuration.html#plugins)

  1)[MyBatis Generator Core](https://mvnrepository.com/artifact/org.mybatis.generator/mybatis-generator-core)

  2)[MyBatis Plus](https://mvnrepository.com/artifact/com.baomidou/mybatis-plus)

  3)通用mapper



## 7、映射器（mappers）

MapperRegistry: 注册绑定Mapper文件

方式一: 使用指定路径绑定注册(推荐使用)

```xml
<!--每一个mapper.xml都需要在Mybatis核心配置文件中注册-->
<mappers>
    <mapper resource="com/ggs/dao/UserMapper.xml"/>
</mappers>
```

方式二: 试用class文件绑定注册

```xml
<!--每一个mapper.xml都需要在Mybatis核心配置文件中注册-->
<mappers>
	<mapper class="com.ggs.dao.UserMapper"></mapper>
</mappers> 
```

方式二注意点: 

- 接口和Mapper配置文件必须同名
- 接口和Mapper配置文件必须在同一个包下



方式三: 使用扫描包进行注入绑定

```xml
<!--每一个mapper.xml都需要在Mybatis核心配置文件中注册-->
<mappers>
    <package name="com.ggs.dao"/>
</mappers>
```

方式三注意点: 

- 接口和Mapper配置文件必须同名
- 接口和Mapper配置文件必须在同一个包下

------

## 8、生命周期

官方文档:

#### **作用域（Scope）和生命周期**

​		理解我们之前讨论过的不同作用域和生命周期类别是至关重要的，因为错误的使用会导致非常严重的==并发问题==。

#### SqlSessionFactoryBuilder

- 这个类可以被实例化、使用和丢弃，一旦创建了 SqlSessionFactory，就不再需要它了。 

- 因此 SqlSessionFactoryBuilder 实例的最佳作用域是方法作用域（也就是局部方法变量）。

#### SqlSessionFactory

- SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。 (可以帮这个SqlSessionFactory想象成连接池,从中获取连接(sqlSession))

- 使用 SqlSessionFactory 的最佳实践是在应用运行期间不要重复创建多次，多次重建 SqlSessionFactory 被视为一种代码“坏习惯”。
- 因此 SqlSessionFactory 的最佳作用域是==应用作用域==(ApplicationContext)。
- 最简单的就是使用==单例模式==或者==静态单例模式==。

#### SqlSession

- 连接到连接池的一个请求
- SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域。
- 用完之后需要赶紧关闭,否则资源被占用(关闭会被重新放入连接池(sqlSessionFactory)中)

----------



# 5、解决属性名和字段名不一致的问题

## 1、问题

数据库字段

![image-20200616135352182](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200616135355.png)



新建一个项目, 拷贝之前项目的内容, 修改实体类字段, 修改成于数据库表的不一致

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
//给JavaBean起别名,在mapper.xml文件中引用
@Alias(value = "hello")
public class User {
  private int id;
  private String username;
  private String password;
}
```

getById查询结果

![image-20200616140342800](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200616140345.png)



解决方法: 

- 起别名: 

```xml
<select id="getById" resultType="com.ggs.pojo.User" parameterType="int">
    select id,pwd password,name username from user where id=#{id}
</select>
```

结果:

![image-20200616140450384](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200616140451.png)



## 2、resultMap映射

```xml
<resultMap id="UserMap" type="User">
    <result property="id" column="id"/>
    <result property="username" column="name"/>
    <result property="password" column="pwd"/>
</resultMap>
<select id="getById" resultMap="UserMap" parameterType="int">
    select * from user where id=#{id}
</select>
```

官方文档片段:

`resultMap` 元素是MyBatis 中最重要最强大的元素。

`resultMap` 的设计思想是，对简单的语句做到零配置，对于复杂一点的语句，只需要描述语句之间的关系就行了。

如果这个世界总是这么简单就好了。



------------

# 6.日志

6.1、日志工厂

如果一个数据库操作,出现了异常,我们需要拍错,就需要查看日志

层级: sout、debug

现在: 日志工厂

![image-20200616231049997](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200616231059.png)

- SLF4J 
- LOG4J [掌握]
- LOG4J2 
- JDK_LOGGING 
- COMMONS_LOGGING 
- STDOUT_LOGGING   [掌握]
- NO_LOGGING

在Mybatis中具体是用那个日志实现, 在设置中设定

**STDOUT_LOGGING标注日志输出**

```xml
<settings>
    <setting name="logImpl" value="STDOUT_LOGGING"/>
</settings>
```































