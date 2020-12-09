自动配置：zk + db

core：zk操作 + db操作(web端与配置无关的db操作放在web里)

client：用户监听配置变化，自定义一些额外操作

高可用：管理台高可用
1. 利用zk，创建临时顺序节点
2. Apache Ratis
3. spring-integration

<dependency>
    <groupId>org.springframework.integration</groupId>
    <artifactId>spring-integration-zookeeper</artifactId>
    <version>5.3.3.RELEASE</version>
</dependency>

https://blog.csdn.net/weixin_43838174/article/details/106839669
https://www.jianshu.com/p/b9e93925a63a/

程序应用读取云端分布式配置中心配置文件（和配置中心建立长连接）

client流程：
1. 访问web端提供的http接口初始化配置
2. 开启zk节点监听，根据节点变化，拉取最新配置
4. 用户可扩展：监听配置变化(涉及到的path + 具体的配置)，是否可以用户自定义覆盖默认的逻辑？

更新配置流程：
1. zk监听到，从web拿到新配置
2. 根据namespace全路径，定位Spring容器里的那个配置类
3. 利用反射修改成员变量

web流程：
1. 读取数据库初始化zk节点
2. 提供正常的增删查改操作，并修改相应的zk节点
3. 本地缓存？如果加了本地缓存，修改了配置之后也要更新本地缓存
4. 给client提供查询接口以便更新配置


由于初始化和监听的逻辑不同，两个starter

starter：给客户端用

starter和web后台都依赖core

starter->autoconfig->client->core


数据库：连接池


com.mysql.cj.jdbc.Driver
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.22</version>
</dependency>


com.mysql.jdbc.Driver
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.49</version>
</dependency>


1. 获取连接 Connection con = DriverManager.getConnection()
2. 开启事务 con.setAutoCommit(true/false);
3. 执行CRUD
4. 提交事务/回滚事务 con.commit() / con.rollback();
5. 关闭连接 conn.close();

OkHttp:https://square.github.io/okhttp/


```sql

CREATE TABLE `namespace` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `env_id` bigint(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '1-p;2-e;3-n',
  `username` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_flag` int(11) DEFAULT '1' COMMENT '1-normal;2-deleted',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;

CREATE TABLE `namespace_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_key` varchar(255) NOT NULL COMMENT '键',
  `config_value` varchar(255) NOT NULL COMMENT '值',
  `namespace_id` bigint(20) NOT NULL COMMENT '所属namespace',
  `username` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_flag` int(11) DEFAULT '1' COMMENT '1-normal;2-deleted',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

```
存在多个用户拥有相同的权限，在分配的时候就要分别为这几个用户指定相同的权限，修改时也要为这几个用户的权限进行一一修改。有了角色后，我们只需要为该角色制定好权限后，将相同权限的用户都指定为同一个角色即可，便于权限管理。

对于批量的用户权限调整，只需调整用户关联的角色权限，无需对每一个用户都进行权限调整，既大幅提升权限调整的效率，又降低了漏调权限的概率。

```sql

```
