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

![image-20200616122313664](E:%5CideaIU-2020.1%5Cworkspace%5Cmybatis3%5CREADME.assets%5Cimage-20200616122313664.png)



重要的两个: cacheEnabled是开启缓存功能, lazyLoadingEnabled是开启懒加载

作用: 提高效率

![image-20200616122439339](E:%5CideaIU-2020.1%5Cworkspace%5Cmybatis3%5CREADME.assets%5Cimage-20200616122439339.png)



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

## 6.1、日志工厂

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

--------

## 6.2、Log4j

什么是Log4j?

- Log4j是[Apache](https://baike.baidu.com/item/Apache/8512995)的一个开源项目，通过使用Log4j，我们可以控制日志信息输送的目的地是[控制台](https://baike.baidu.com/item/控制台/2438626)、文件、[GUI](https://baike.baidu.com/item/GUI)组件
- 我们也可以控制每一条日志的输出格式
- 通过定义每一条日志信息的级别，我们能够更加细致地控制日志的生成过程
- 通过一个[配置文件](https://baike.baidu.com/item/配置文件/286550)来灵活地进行配置，而不需要修改应用的代码。



1.先导入log4j包

```xml
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```

2.log4j.properties

```properties
log4j.rootLogger = debug,stdout,file,error
log4j.additivity.org.apache=true

log4j.appender.stdout = org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target = System.out
log4j.appender.stdout.threshold = debug
log4j.appender.stdout.layout = org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern = [%-5p] %d{yyyy-MM-dd HH:mm:ss,SSS} method:%l%n%m%n

log4j.appender.file = org.apache.log4j.DailyRollingFileAppender
log4j.appender.file.File = E:/ideaIU-2020.1/workspace/mybatis3/logs/log.log
log4j.appender.file.Append = true
log4j.appender.file.Threshold = DEBUG 
log4j.appender.file.layout = org.apache.log4j.PatternLayout
log4j.appender.file.DatePattern = '.'yyyy-MM-dd-HH-mm
log4j.appender.file.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n

log4j.appender.error = org.apache.log4j.DailyRollingFileAppender
log4j.appender.error.File =E:/Eclipse2020.4/workspace/shop/logs/error.log 
log4j.appender.error.Append = true
log4j.appender.error.Threshold = debug 
log4j.appender.error.layout = org.apache.log4j.PatternLayout
log4j.appender.file.DatePattern = '.'yyyy-MM-dd-HH-mm
log4j.appender.error.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n
```

3.配置log4j为日志实现

```xml
<settings>
    <setting name="logImpl" value="LOG4J"/>
</settings>
```

4.运行测试案例

![](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200617232419.png)



**Log4j的简单实用**

1.导入org.apache.log4j.Logger; 不要导错了

2.日志对象, 参数为当前类的class

```java
static Logger logger = Logger.getLogger(UserDaoTest.class);
```



3.打印日志

```java
@Test
public void testLog4j() {
    logger.trace("trace级别日志");
    logger.info("info级别日志");
    logger.debug("debug级别日志");
    logger.warn("warn级别日志");
    logger.error("error级别日志");
}
```

4.输出

![](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200617232318.png)

----



# 7.分页

**好处: 减少数据量**

## **7.1、使用Limit分页**

```xml
<!--分页-->
<select id="getUserByLimit" parameterType="map" resultMap="UserMap">
    select * from mybatis.user limit #{start},#{pageSize}
</select>
```



## 7.2、RowBounds分页

在java代码层面进行分页

1.接口

```java
List<User> getUserByRowBounds();
```

2.mapper.xml

```xml
<select id="getUserByRowBounds" resultMap="UserMap">
    select * from  user;
</select>
```

3.测试用例

```java
@Test
public void getUserByRowBoundsTest() {
    SqlSession sqlSession = MyBatisUtils.getSqlSession();
    RowBounds rowBounds = new RowBounds(3, 3);
    //通过java代码层面进行分页
    List<User> userList = sqlSession.selectList("com.ggs.dao.UserMapper.getUserByRowBounds", null, rowBounds);
    userList.forEach(System.out::println);
}
```



## 7.3、分页插件

![](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200618233346.png)

1.导入jar包

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>5.1.11</version>
</dependency>
```

2.接口

```java
List<User> getAll();
```

3.mapper.xml

```xml
<select id="getAll" resultMap="UserMap">
    select * from user
</select>
```

4.测试用例, 只需要指定PageHelper之后, 分页操作就自动完成了,我们只需要写业务代码即可

```java
@Test
public void pageHelperTest(){
    SqlSession sqlSession = MyBatisUtils.getSqlSession();
//开启分页查询,参数一: pageNum(从第几条开始查询),pageSize(查询多少条)
    PageHelper.startPage(2,2);
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    List<User> all = mapper.getAll();
    all.forEach(System.out::println);
}
```

5.结果

数据库中的数据

![](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200618234643.png)

分页查询出的数据

![](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200618234234.png)



--------

# 8、注解开发

## CRUD操作

8.1注解开发

1.注解在接口上实现

```java
@Select("select * from user")
List<User> getUsers();
```

2.在核心配置文件中绑定接口

```xml
<!--绑定接口-->
<mappers>
    <mapper class="com.ggs.dao.UserMapper"/>
</mappers>
```

3.测试

```java
/**
     * sqlSession.getMapper(**.class)
     * 最终通过jdk动态代理生成代理对象
     * 可以在org.apache.ibatis.binding.MapperProxyFactory中的newInstance方法看到执行过程
     */
@Test
public void test1(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    List<User> users = mapper.getUsers();
    users.forEach(System.out::println);
}
```

本质: 反射机制实现

底层: 动态代理



在工具类创建的时候设置自动提交事务

```java
public static SqlSession getSqlSession() {
    return sqlSessionFactory.openSession(true);
}
```



UserMapper.java

```java
@Insert("insert into user(id,name,pwd) values(#{id},#{name},#{pwd})")
int insertUser(User user);

@Update("update user set name=#{name},pwd=#{pwd} where id=#{id}")
int updateUser(User user);

@Delete("delete from user where id =#{id}")
int deleteUser(int id);
```

测试用例

```java
@Test
public void testInsertUser() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    mapper.insertUser(new User(5, "lucy", "4156135413"));
}

@Test
public void testUpdateUser() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    mapper.updateUser(new User(5, "xxx", "skldfjklsdjf"));
}

@Test
public void testDeleteUser() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    mapper.deleteUser(5);
}
```



**关于@Param(value="")注解**

- 基本数据类型或Stirng类型, 需要加上
- 引用类型不用加
- 如果只有一个基本数据类型或者String类型,可以忽略,但建议加上
- sql语句中的#{}引入的名字就是在@Param中指定的名字



-------------------------



# 9、多对一查询

## 方式一:按照查询嵌套处理

**1.创建两张表,一张老师表,一张学生表**

![](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200621195634.png)

![](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200625163626.png)

学生和老师是多对一的关系, 学生表tid为外键,指向老师表的id

对应的JaveBean对象

Teacher类

```java
@Data
public class Teacher {
    private int id;
    private String name;
}
```

Student类

```java
@Data
public class Student {
    private int id;
    private String name;
    //多对一,关联一个老师对象
    private Teacher teacher;
}
```



**2.编写mapper.xml文件**

StudentResultMap是关键: 

​	association标签,

​	property: 指定Student类中的属性名

​	column: 指定Student类对应的数据库表的字段名

​	javaType: 指定该属性的类型,Teacher类型

​	select: 指定select语句, 将该select语句的返回结果封装到teacher属性中



```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.ggs.dao.StudentMapper">
    <!--方式一: 按照查询嵌套处理-->
    <select id="getTeacher" resultType="Teacher">
        select * from teacher where id=#{id}
    </select>

    <resultMap id="StudentResultMap" type="Student">
        <result property="id" column="id"></result>
        <result property="name" column="name"></result>
        <!--复杂的属性,需要单独处理
            对象: association
            集合: collection-->
        <association property="teacher" column="tid" javaType="Teacher" select="getTeacher"></association>
    </resultMap>

    <select id="getAllStudent" resultMap="StudentResultMap">
        select * from student;
    </select>
</mapper>
```

**3.测试用例**

```java
@Test
public void getAllStudentTest(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
    List<Student> studentList = mapper.getAllStudent();
    studentList.forEach(System.out::println);
}
```

**4.结果**

![](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200621201733.png)

-------------

## 方式二:按照结果嵌套处理

**1.mapper.xml配置文件**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.ggs.dao.StudentMapper">
    <!--方式二: 按照结果嵌套处理-->
    <select id="getAllStudent2" resultMap="StudentResultMap2">
        SELECT
            s.id sid,
            s.NAME sname,
            s.tid tid,
            t.NAME tname
        FROM
            student s
            LEFT JOIN teacher t ON s.tid = t.id;
    </select>

    <resultMap id="StudentResultMap2" type="Student">
        <result property="id" column="sid"/>
        <result property="name" column="sname"/>
        <association property="teacher" javaType="Teacher">
            <result property="id" column="tid"/>
            <result property="name" column="tname"/>
        </association>
    </resultMap>
</mapper>
```

**2.测试用例**

```java
@Test
public void getAllStudent2Test(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
    List<Student> studentList = mapper.getAllStudent2();
    studentList.forEach(System.out::println);
}
```

**3.结果**

![](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200621201733.png)



多对一查询方式:

- 子查询
- 联表查询



# 10、一对多查询

**修改JavaBean对象的属性**

Student类

```java
@Data
public class Student {
    private int id;
    private String name;
    //多对一,关联一个老师对象
    private int tid;
}
```

Teacher类

```java
@Data
public class Teacher {
    private int id;
    private String name;
    private List<Student> students;
}
```



## 方式一:按照结果嵌套处理

复杂属性:

​	对象: association

​	集合: collection

​		javaType: 指定返回值类型

​		ofType: 指定集合泛型

mapper.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.ggs.dao.TeacherMapper">
    <select id="getTeachers" resultMap="TeacherResultMap">
        SELECT
            s.id sid,
            s.NAME sname,
            t.id tid,
            t.NAME tname
        FROM
            teacher t
            LEFT JOIN student s  ON s.tid = t.id
    </select>
    <resultMap id="TeacherResultMap" type="Teacher">
        <result property="id" column="tid" />
        <result property="name" column="tname"/>
        <collection property="students" ofType="Student" >
            <result property="id" column="sid"/>
            <result property="name" column="sname"/>
            <result property="tid" column="id"/>
        </collection>
    </resultMap>
</mapper>
```

结果:

![](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200621214128.png)



## 方式二:按照查询嵌套处理

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.ggs.dao.TeacherMapper">
    <select id="getTeachers2" resultMap="TeacherResultMap2">
        select t.id,t.name from teacher t
    </select>
    <resultMap id="TeacherResultMap2" type="Teacher">
        <result property="id" column="id"/>
        <result property="name" column="name"/>
        <collection property="students" javaType="java.util.List"  ofType="Student" select="getStudents" column="id" ></collection>
    </resultMap>

    <select id="getStudents" resultType="Student">
        select * from student s where tid=#{tid}
    </select>
</mapper>
```

结果:

![](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200621215938.png)



-----------------

# 11、动态SQL

**什么是动态SQL?**

​	动态SQL就是根据不同的条件生成不同的SQL语句

**官方文档:** 

​	动态 SQL 是 MyBatis 的强大特性之一。如果你使用过 JDBC 或其它类似的框架，你应该能理解根据不同条件拼接 SQL 语句有多痛苦，例如拼接时要确保不能忘记添加必要的空格，还要注意去掉列表最后一个列名的逗号。利用动态 SQL，可以彻底摆脱这种痛苦。



**搭建环境**

```sql
CREATE TABLE blog (
	id VARCHAR ( 50 ) NOT NULL COMMENT '博客id',
	title VARCHAR ( 100 ) NOT NULL COMMENT '博客标题',
	author VARCHAR ( 30 ) NOT NULL COMMENT '博客作者',
	create_time DATETIME NOT NULL COMMENT '创建时间',
	views INT ( 30 ) NOT NULL COMMENT '浏览量' 
) ENGINE = INNODB DEFAULT charset = utf8
```

Blog实体类

```java
@Data
public class blog {
    private String id;
    private String title;
    private String author;
    private Date createTime;
    private int views;
}
```



## ①if标签

mapper.xml

```
    <select id="queryBlogIf" parameterType="map" resultType="Blog">
        select * from blog where 1=1
        <if test="title != null">
--             and title like '%${title}%'  
--             and title like concat('%','${title}','%')
            and title like concat('%',#{title},'%')
        </if>
        <if test="author != null">
            and author = #{author}
        </if>
    </select>
```

-- 是表示注释的意思

模糊查询有四种方式

​	第一种: 在执行放入sql前,先将参数前后提前加上%号

​	第二种: title like '%${title}%'  , ${}这种方式是拼接字符串, 会有sql注入风险

​	第三种: title like concat('%','${title}','%') 借助sql函数,防止sql注入

​	第四种: title like concat('%',#{title},'%') 借助sql函数,防止sql注入

提示: ${} 和 #{}的区别, $符号字段, 放入sql语句中后,是没有''包裹的,而#{}的会自动添加'', 包裹住属性值



## ②where标签

改写if标签中的查询语句

```xml
<select id="queryBlogIf" parameterType="map" resultType="Blog">
    select * from blog
    <where>
        <if test="title != null">
            and title like concat('%',#{title},'%')
        </if>
        <if test="author != null">
            and author = #{author}
        </if>
    </where>
</select>
```

where标签: 当where标签中的有一个或多个条件匹配的时候,会在sql语句上自动添加where关键字, 而且, where标签中的if标签, 会自动删除第一个条件前的and关键字

例子:

如果if标签都不满足,那么输入的sql语句为:

```sql
select * from blog 
```

如果满足一条或多条, 自动添加where,并且清除第一个and

```sql
select * from blog WHERE title like concat('%',?,'%') and author = ? 
```

**官方文档解释:**

<font color=red>*where* 元素只会在子元素返回任何内容的情况下才插入 “WHERE” 子句。而且，若子句的开头为 “AND” 或 “OR”，*where* 元素也会将它们去除。</font>

------------

## ③where+choose+when

官方文档:

<font color=red>有时候，我们不想使用所有的条件，而只是想从多个条件中选择一个使用。针对这种情况，MyBatis 提供了 choose 元素，它有点像 Java 中的 switch 语句</font>

choose标签中, 只会使用第一个成立条件的内容

```xml
<select id="queryBlogChoose" parameterType="map" resultType="Blog">
    select * from blog
    <where>
        <choose>
            <when test="author != null">
                author = #{author}
            </when>
            <when test="title != null">
                title = #{title}
            </when>
            <otherwise>
                views = #{views}
            </otherwise>
        </choose>
    </where>
</select>
```



## ④set标签

<font color=red>官方文档: 用于动态更新语句的类似解决方案叫做 *set*。*set* 元素可以用于动态包含需要更新的列，忽略其它不更新的列。set* 元素会动态地在行首插入 SET 关键字，并会删掉额外的逗号（这些逗号是在使用条件语句给列赋值时引入的）。</font>



```xml
<update id="updateBlog" parameterType="map" >
    update blog
    <set>
        <if test="title != null">title=#{title},</if>
        <if test="author != null">author=#{author},</if>
        <if test="create_time != null">create_time=#{createTime},</if>
        <if test="views != null">views=#{views},</if>
    </set>
    <where>
        id=#{id}
    </where>
</update>
```



## ⑤SQL片段

有的时候，我们可能会将一些功能的部分抽取出来,方便复用, 使用SQL标签抽取公共的部分

**1.声明片段**

```xml
<sql id="if-title-author">
    <if test="title != null">
        and title like concat('%',#{title},'%')
    </if>
    <if test="author != null">
        and author = #{author}
    </if>
</sql>
```

**2.引用SQL片段, 在合适的地方使用include标签进行引用**

```xml
<select id="queryBlogIf" parameterType="map" resultType="Blog">
    select * from blog
    <where>
        <include refid="if-title-author"></include>
    </where>
</select>
```



注意事项:

- 最好基于单表来定义SQL片段!
- 不要存在where标签



## ⑥foreach片段

官方文档

![](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200625163259.png)

案例

mapper.xml

```xml
<select id="queryBlogForeach" parameterType="map" resultType="com.ggs.pojo.Blog">
    select * from blog
    <where>
        <foreach collection="ids" item="id" open="(" separator="or" close=")">
            id = #{id}
        </foreach>
    </where>
</select>
```

测试案例

```java
@Test
public void testQueryBlogForeach(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
    Map<String, Object> map = new HashMap<>();
    List ids=new ArrayList<>();
    map.put("ids",ids);
    ids.add("3114f6a3b0894fbaa592f69cf6e93c4c");
    ids.add("50a43483f3f0446b8b6db375c939858d");
    ids.add("5a783c8d94664f20a77cd49bef8c61bf");
    List<Blog> blogs = mapper.queryBlogForeach(map);
    blogs.forEach(System.out::println);
}
```

-----------------------



# 12、缓存

## 12.1、什么是缓存[ Cache ]?

- 存在内存中的临时数据。

- 将用户经常查询的数据放在缓存(内存)中，用户去查询数据就不用从磁盘上(关系型数据库数据文件)查询，从缓存中查询,从而提高查询效率,解决了高并发系统的性能问题。

2.为什么使用缓存?

- 减少和数据库的交互次数,减少系统开销，提高系统效率。

3.什么样的数据能使用缓存?

- 经常查询并且不经常改变的数据。



## 12.2、Mybatis缓存

- MyBatis包含- 个非常强大的查询缓存特性， 它可以非常方便地定制和配置缓存。缓存可以极大的提升查询效率。

- MyBatis系统中默认定义了两级缓存: **一级缓存**和**二级缓存**
  - 默认情况下，只有- -级缓存开启。 (SqlSession级别的缓存， 也称为本地缓存)
  - 二级缓存需要手动开启和配置，他是基于namespace级别的缓存.
  - 为了提高扩展性，MyBatis定义了缓存接口Cache.我们可以通过实现Cache接口来自定义二级缓存



## 12.3、一级缓存

- ==一级缓存==也叫==本地缓存==:
  - mybatis默认开启一级缓存
  - 与数据库同一次会话期间查询到的数据会放在本地缓存中。
  - 以后如果需要获取相同的数据，直接从缓存中拿，没必须再去查询数据库;



测试步骤:

①开启日志

②测试在同一个Session中查询两次相同的记录

③查看日志输出

测试仪一: 两次查询相同的数据

![](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200625173729.png)

测试二: 在两次两次相同的插叙之间, 进行一次修改

在同一次会话中, 即使查询同一条数据, 如果中间出现增删改, 都会刷新缓存

![](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200625173706.png)



缓存失效的情况, 官方文档:

- 映射语句文件中的所有 select 语句的结果将会被缓存。
- 映射语句文件中的所有 insert、update 和 delete 语句会刷新缓存。
- 缓存会使用最近最少使用算法（LRU, Least Recently Used）算法来清除不需要的缓存。
- 缓存不会定时进行刷新（也就是说，没有刷新间隔）。
- 缓存会保存列表或对象（无论查询方法返回哪种）的 1024 个引用。
- 缓存会被视为读/写缓存，这意味着获取到的对象并不是共享的，可以安全地被调用者修改，而不干扰其他调用者或线程所做的潜在修改。



缓存失效的情况, 人话文档:

1. 查询不同的数据
2. 增删改查操作, 可能会改变原来数据, 所以必定会刷新缓存
3. 查询不同的Mapper.xml
4. 手动清理缓存

```java
sqlSession.clearCache();//手动清理缓存
```



小结: 一级缓存默认是开启的, 只有在一次SqlSession中有效, 也就是拿到连接到关闭连接的时间段之间



## 12.4、二级缓存

<font color=red>官方文档</font>

​		MyBatis 内置了一个强大的事务性查询缓存机制，它可以非常方便地配置和定制。 为了使它更加强大而且易于配置，我们对 MyBatis 3 中的缓存实现进行了许多改进。

​		默认情况下，只启用了本地的会话缓存，它仅仅对一个会话中的数据进行缓存。 要启用全局的二级缓存，只需要在你的 SQL 映射文件中添加一行：

```xml
<cache/>
```

​		缓存只作用于 cache 标签所在的映射文件中的语句。如果你混合使用 Java API 和 XML 映射文件，在共用接口中的语句将不会被默认缓存。你需要使用 @CacheNamespaceRef 注解指定缓存作用域。

----------



笔记: 

- 二级缓存也叫全局缓存，一级缓存作用域太低了,所以诞生了二级缓存
- 基于namespace级别的缓存，-个名称空间，对应一个二级缓存;
- 工作机制
  - 一个会话查询- -条数据，这个数据就会被放在当前会话的一-级缓存中;
  - 如果当前会话关闭了，这个会话对应的一-级缓存就没了; 但是我们想要的是，会话关闭了，一级缓存中的数据被保存到二级缓存中;
  - 新的会话查询信息，就可以从二级缓存中获取内容;
  - 不同的mapper查出的数据会放在自己对应的缓存(map)中;



测试步骤:

①开启全局缓存, 这个配置默认就是true, 显式先出来提高阅读性(可选)

```xml
<!--显式的开启全局缓存-->
<setting name="cacheEnabled" value="true"/>
```

②在Mapper.xml文件中添加缓存标签(==必须==)

不添加参数, 使用默认配置

```xml
<!--在当前Mapper.xml中使用二级缓存-->
<cache/>
```



自定义参数

```xml
<!--在当前Mapper.xml中使用二级缓存-->
<cache eviction="FIFO"
       flushInterval="60000"
       size="512"
       readOnly="true"/>
```

==eviction==: 清除策略, 默认使用LRU

- `LRU` – 最近最少使用：移除最长时间不被使用的对象。
- `FIFO` – 先进先出：按对象进入缓存的顺序来移除它们。
- `SOFT` – 软引用：基于垃圾回收器状态和软引用规则移除对象。
- `WEAK` – 弱引用：更积极地基于垃圾收集器状态和弱引用规则移除对象。

==flushInterval==: 刷新间隔, 属性可以被设置为任意的正整数，设置的值应该是一个以毫秒为单位的合理时间量。 默认情况是不设置，也就是没有刷新间隔，缓存仅仅会在调用语句时刷新。

==size==: 引用数目, 属性可以被设置为任意正整数，要注意欲缓存对象的大小和运行环境中可用的内存资源。默认值是 1024。



==readOnly==: 只读, 属性可以被设置为 true 或 false。只读的缓存会给所有调用者返回缓存对象的相同实例。 因此这些对象不能被修改。这就提供了可观的性能提升。而可读写的缓存会（通过序列化）返回缓存对象的拷贝。 速度上会慢一些，但是更安全，因此默认值是 false。

 

<font color=red>二级缓存是事务性的。这意味着，当 SqlSession 完成并提交时，或是完成并回滚，但没有执行 flushCache=true 的 insert/delete/update 语句时，缓存会获得更新。</font>



测试用例

```java
@Test
public void test2() {
    //创建两个sqlSession,分别获取UserMapper
    SqlSession sqlSession1 = MybatisUtils.getSqlSession();
    UserMapper mapper1 = sqlSession1.getMapper(UserMapper.class);
    SqlSession sqlSession2 = MybatisUtils.getSqlSession();
    UserMapper mapper2 = sqlSession2.getMapper(UserMapper.class);

    User user1 = mapper1.queryUserById(2);
    System.out.println(user1);
    sqlSession1.close(); //重点, 要关闭sqlSession1,否则数据值只保存在本地缓存中, 而不会保存到二级缓存中, 下面的的mapper2会再次去数据库查询

    User user2 = mapper2.queryUserById(2);
    System.out.println(user2);
    sqlSession2.close();

    System.out.println(user1 == user2);
}
```

结果: 

1. 在cache标签中设置readOnly为true, 两次查询的结果都是同一个对象, 比较后返回true

2. 如果readOnly设置为false, 返回的则是一个新的对象, 并且该对象必须要实现序列化接口,否则报错

   <font color=red>org.apache.ibatis.cache.CacheException: Error serializing object.  Cause: java.io.NotSerializableException: com.ggs.pojo.User</font>

![](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200625181029.png)



小结:

- 只要开启了二级缓存，在同一个Mapper下就有效I
- 所有的数据都会先放在一级缓存中;
- 只有当会话提交,或者关闭的时候，才会提交到二级缓存





## 12.5、缓存原理

![](https://gitee.com/starbug-gitee/PicBed/raw/master/img/20200625184348.png)



