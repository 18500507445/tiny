## redis多数据源

### yml配置
~~~yml
spring:
  redis:
    # redis数据库索引（默认为0）
    database: 0
    # redis服务器地址
    host: xx.xx.xxx.xxx
    # redis服务器连接端口
    port: 1
    # redis服务器连接密码（尽量设置密码，防止远程注入服务器挖矿）
    password: 1
    lettuce:
      pool:
        # 连接池最大连接数（使用负值表示没有限制）
        max-active: -1
        # 连接池最大阻塞等待时间（使用负值表示没有限制）
        max-wait: 3000
        # 连接池中的最大空闲连接
        max-idle: 8
        # 连接池中的最小空闲连接
        min-idle: 0
    #连接超时时间
    timeout: 10000

  redis2:
    database: 1
~~~

### 代码
~~~java
//第二个数据源
@Resource
private RedisTemplate<String, Object> secondRedisTemplate;
~~~