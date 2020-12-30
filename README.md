###工程结构
- pixelcat-web 管理台
- pixelcat-web-ui 管理台前端
- pixelcat-test 标准客户端使用

###功能
1. 配置实时更新，粒度到namespace
2. 管理台高可用集群
3. 客户端自定义配置变化监听

高可用：管理台高可用
1. 利用zk，创建临时顺序节点
2. [Apache Ratis](https://ratis.incubator.apache.org/#gettingstarted)
3. [spring-integration](https://docs.spring.io/spring-integration/docs/current/reference/html/)
4. [一致性：JRaft](https://www.sofastack.tech/projects/sofa-jraft/jraft-user-guide/)

```xml
<dependency>
    <groupId>org.springframework.integration</groupId>
    <artifactId>spring-integration-zookeeper</artifactId>
    <version>5.3.3.RELEASE</version>
</dependency>
```

Nacos和Apollo配置推送都是基于HTTP长轮询，客户端和配置中心建立HTTP长连接，当配置变更的的时候，配置中心把配置推送到客户端。

###[zookeeper应用](https://zhuanlan.zhihu.com/p/59669985) [zookeeper应用](https://www.cnblogs.com/ynyhl/p/9981887.html)
0. 通知机制：https://www.cnblogs.com/wuzhenzhao/p/9994450.html
1. 命名服务（生成顺序节点，分布式ID）
2. 配置管理（数据发布/订阅）
3. 集群管理（创建顺序临时节点）
4. 分布式锁（创建临时节点）
5. 队列
6. 心跳检测（被检测的客户端创建临时节点）
7. Master 选举（集群机器都尝试创建节点，创建成功的客户端机器就会成为 Master，失败的客户端机器就在该节点上注册一个 Watcher 用于监控当前 Master 机器是否存活，一旦发现 Master 挂了，其余客户端就可以进行选举了。）

高可用流程
1. 所有管理台，启动的时候，向zk注册一个临时节点。成功创建节点的为Master。
2. 所有管理台，监听master节点变化。
3. Master挂掉，临时节点删除，其余节点重新选举。

客户端请求管理台集群，怎样得知当前提供服务的是哪一个？
1. 过滤器，从zk获取当前master是哪一个，然后把请求转给master(这种思路是对的)
2. [zookeeper一致性知识点](https://www.cnblogs.com/aspirant/p/9179045.html)

用户访问管理台前端？
- 访问任意一个节点都可

client更新配置流程：
1. 监听zk节点变化事件，发生事件后解析出 项目-环境-namespace 结构，向管理台发出http请求相应的数据，封装成ConfigEvent，利用ConfigSubject传播给对应的收听者。
2. BeanPostProcessor过滤标记@PixelCatConfig注解的配置类，解析 项目-环境-namespace，将bean和namespace事件绑定在一起
3. 当接受到ConfigSubject传播的事件后，读取bean的属性，利用反射从属性的注解解析出key，从事件中利用key获取对应的value，赋值给属性

管理台流程：
1. 选举成功的，读取数据库初始化zk节点
2. 提供正常的增删查改操作，并修改相应的zk节点
3. 给client提供查询接口以便更新配置
4. Master挂掉，其它节点监听到，自己尝试创建Master节点。成功后，初始化配置节点，继续提供服务

客户端配置
1. 配置项目ID
2. 配置环境ID
3. 书写配置类，指明namespace，获取kv


- [Binder](https://www.cnblogs.com/lyldelove/p/13431115.html)
- 动态注册Bean
- environment.resolvePlaceholders(name)


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
