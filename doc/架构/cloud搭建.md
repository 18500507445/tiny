## spring-cloud学习

### 简介:
* 基于订单和支付业务简单的搭建一个spring cloud alibaba项目
* [Spring cloud和Alibaba版本对照](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E)
~~~xml
 <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-dependencies</artifactId>
    <version>2021.0.4</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>

<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>2021.0.4</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
~~~

### 1. Nacos（服务注册和配置中心）
[注册中心nacos服务端下载](https://github.com/alibaba/nacos)

项目中下载的2.2.2版本，导入项目中，然后添加一个shell script单节点配置
本地添加nacos，如图 [访问地址](http://localhost:8848/nacos/)
![nacos](https://cdn.jsdelivr.net/gh/18500507445/drawing-bed/tiny/nacos.png)


服务pom添加依赖
~~~xml
springboot
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
</dependency>

springcloud
<!-- 注册nacos -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>

<!-- 配置中心 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
~~~

配置动态刷新注解@RefreshScope，@Value取到的值就是动态改变的

### 2.Gateway（网关，路由转发到服务）
~~~yml
spring:
  cloud:
    gateway:
      # 配置路由列表，每一项都包含了很多信息
      routes:
      - id: orderServer   # 路由名称
        uri: lb://orderServer  # 路由的地址，lb表示使用负载均衡到微服务，也可以使用http正常转发
        predicates: # 路由规则，断言什么请求会被路由
        - Path=/api/order/**  # 只要是访问的这个路径，一律都被路由到上面指定的服务
~~~

### 3. OpenFeign（远程调用）
~~~xml
<!-- 远程调用openfeign -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
~~~

orderService需要用到payService的接口，pom引入payService依赖，orderService创建biz目录，添加注解@FeignClient("payServer")和配置文件服务名称一致
~~~java
@FeignClient("payServer")
public interface PayOrderClient {
    @RequestMapping("/api/getPayOrder/{id}")
    PayOrder getPayOrder(@PathVariable("id") Long id);
}
~~~

OrderApplication添加@EnableFeignClients注解开启远程调用

### 4.Loadbalancer（负载均衡，默认轮询）
~~~xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
~~~

### 5.Sentinel（流量防卫兵，和Nacos一样需要单独下载部署）
[中文文档](https://sentinelguard.io/zh-cn/docs/introduction.html)

本地添加sentinel，如图[访问地址](http://localhost:8858/#/login)
![sentinel](https://cdn.jsdelivr.net/gh/18500507445/drawing-bed/tiny/sentinel.png)
[访问地址 账号密码：sentinel](http://localhost:8858/#/login)

然后pom引入依赖
~~~xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>
~~~

#### 5.1 流量控制    
![sentinel](https://cdn.jsdelivr.net/gh/18500507445/drawing-bed/tiny/sentinel-config.png)
流控模式：（1）针对当前接口  （2）当关联接口超阈值会导致当前接口限流（相当于就是别人出错，你背锅）   （3）更细粒度，精确具体方法  
流控效果：（1）快速失败，直接抛出异常FlowException  （2）预热Warm Up按照预热时长缓慢地进入 （3）排队等待

#### 5.2 异常处理
~~~yml
spring:
  cloud:
    sentinel:
      block-page: /api/order/blocked
~~~

~~~java
@RequestMapping("/blocked")
JSONObject blocked() {
    JSONObject object = new JSONObject();
    object.put("code", 403);
    object.put("success", false);
    object.put("massage", "您的请求频率过快，请稍后再试！");
    return object;
}
~~~

流控
![sentinel](https://cdn.jsdelivr.net/gh/18500507445/drawing-bed/tiny/sentinel-config.png)

#### 5.3 热点参数限流
热点规则：针对一个接口中的参数进行策略限流

#### 5.4 服务熔断和降级

#### 5.5 [配置持久化](https://blog.csdn.net/qq_45557455/article/details/125694278?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_utm_term~default-0-125694278-blog-123399569.235^v32^pc_relevant_default_base3&spm=1001.2101.3001.4242.1&utm_relevant_index=3)

### 6. [Seata（分布式事务管理，以order-server服务为例）](https://seata.io/zh-cn/docs/overview/what-is-seata.html)
常规单体服务， 库存扣减->订单创建 在同一个事物中，可以用Transaction保证，但是微服务的话，按照业务模块进行拆分，库存和订单在不同的服务，事物如何保证

[客户端下载地址](https://github.com/seata/seata)

本地配置启动，注意：修改bin目录seata-server.sh jvm参数-Xss改成1M否则启动失败
![nacos](https://cdn.jsdelivr.net/gh/18500507445/drawing-bed/tiny/seata.png)

~~~xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
</dependency>
~~~

file本地文件类型
~~~yml
seata:
  service:
    vgroup-mapping:
        # 这里需要对事务组做映射，默认的分组名为 应用名称-seata-service-group，将其映射到default集群
        # 这个很关键，一定要配置对，不然会找不到服务
      orderService-seata-service-group: default
    grouplist:
      default: localhost:8868
~~~

~~~java
@EnableFeignClients
@EnableAutoDataSourceProxy
@SpringBootApplication
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
~~~

Seata会分析修改数据的sql，同时生成对应的反向回滚SQL，这个回滚记录会存放在undo_log 表中。所以要求每一个Client 都有一个对应的undo_log表（也就是说每个服务连接的数据库都需要创建这样一个表
~~~sql
CREATE TABLE `undo_log`
(
  `id`            BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `branch_id`     BIGINT(20)   NOT NULL,
  `xid`           VARCHAR(100) NOT NULL,
  `context`       VARCHAR(128) NOT NULL,
  `rollback_info` LONGBLOB     NOT NULL,
  `log_status`    INT(11)      NOT NULL,
  `log_created`   DATETIME     NOT NULL,
  `log_modified`  DATETIME     NOT NULL,
  `ext`           VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
~~~

接着我们需要在开启分布式事务的方法上添加@GlobalTransactional注解即可

启动服务观察log
~~~
2023-05-01 22:28:09.525  INFO 58961 --- [           main] i.s.s.a.GlobalTransactionScanner         : Initializing Global Transaction Clients ... 
2023-05-01 22:28:09.589  INFO 58961 --- [           main] i.s.s.a.GlobalTransactionScanner         : Transaction Manager Client is initialized. applicationId[orderServer] txServiceGroup[default_tx_group]
2023-05-01 22:28:09.598  INFO 58961 --- [           main] i.s.s.a.GlobalTransactionScanner         : Resource Manager is initialized. applicationId[orderServer] txServiceGroup[default_tx_group]
2023-05-01 22:28:09.598  INFO 58961 --- [           main] i.s.s.a.GlobalTransactionScanner         : Global Transaction Clients are initialized. 
2023-05-01 22:28:10.055  INFO 58961 --- [           main] i.s.s.a.GlobalTransactionScanner         : Bean[com.order.server.service.impl.OrderServiceImpl] with name [orderServiceImpl] would use interceptor [io.seata.spring.annotation.GlobalTransactionalInterceptor]
~~~

#### 6.1 使用Nacos模式部署
首先Nacos中创建命名空间，稍后会使用命名空间ID
![](./img/命名空间.png)

然后找到seata.conf目录，参考application.example.yml然后替换application.yml配置
~~~yml
seata:
  config:
    # support: nacos 、 consul 、 apollo 、 zk  、 etcd3
    type: nacos
    nacos:
      server-addr: 127.0.0.1:8848
      namespace:
      group: SEATA_GROUP
      username:
      password:
      context-path:
      # 这个不用改，默认就行
      data-id: seataServer.properties
  registry:
    # support: nacos 、 eureka 、 redis 、 zk  、 consul 、 etcd3 、 sofa
    type: nacos
    preferred-networks: 30.240.*
    nacos:
      application: seata-server
      server-addr: 127.0.0.1:8848
      group: SEATA_GROUP
      # 这里就使用我们上面单独为seata配置的命名空间，注意填的是ID
      namespace:
      cluster: default
      # Nacos的用户名和密码
      username:
      password:
      context-path:
      ##if use MSE Nacos with auth, mutex with username/password attribute
~~~

找到源码文件，解压后找到script/config-center/nacos/nacos-config-interactive.sh  

终端执行    
![nacos](https://cdn.jsdelivr.net/gh/18500507445/drawing-bed/tiny/seata-source.png)

然后再Nacos后台看到  
![nacos](https://cdn.jsdelivr.net/gh/18500507445/drawing-bed/tiny/seata-nacos.png)

新建一个配置  

最后yml文件配置，就可以启动啦
~~~yml
seata:
  # 注册
  registry:
    # 使用Nacos
    type: nacos
    nacos:
      # 使用Seata的命名空间，这样才能正确找到Seata服务，由于组使用的是SEATA_GROUP，配置默认值就是，就不用配了
      namespace: a7be9896-1252-41d3-8e9a-96bf83c967b4
      username: nacos
      password: nacos
  # 配置
  config:
    type: nacos
    nacos:
      namespace: a7be9896-1252-41d3-8e9a-96bf83c967b4
      username: nacos
      password: nacos
~~~