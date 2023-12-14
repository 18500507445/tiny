## mq-rabbit配置

### yml配置
~~~yaml
spring:
  rabbitmq:
    addresses: xx.xxx.xxx.xxx:5672 #注意这里是amqp协议端口5672
    username: root #账号
    password: password #密码
    virtual-host: / #虚拟主机环境
    listener:
      simple:
        retry:
          initial-interval: 1000 #1秒后重试
          enabled: true #启用发布重试
          max-attempts: 3 #传递消息的最大尝试次数
          max-interval: 10000 #尝试的最大时间间隔
          multiplier: 1.0 #应用于先前传递重试时间间隔的乘数

    cache:
      channel:
        size: 5

  rabbitmq2:
    addresses: xx.xxx.xxx.xxx:5672
    username: root
    password: password
    virtual-host: /
~~~

### 说明
目前rabbit-starter（ParentRabbitConfig）可以装配2个数据源，第二个需要enable:true开启

### 代码
~~~java

~~~
