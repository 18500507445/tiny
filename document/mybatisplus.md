## mybatis-plus配置

### mybatis-plus动态数据源
~~~yaml
##德鲁伊连接池
spring:
  datasource:
    dynamic:
      primary: master
      strict: false
      datasource:
        master:
          username: username
          password: password
          url: jdbc:mysql://xx.xxx.xxx.xxx:3306/study?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
          driver-class-name: com.mysql.cj.jdbc.Driver
        second:
          username: username
          password: password
          url: jdbc:mysql://xx.xxx.xxx.xxx:3306/my_mall?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
          driver-class-name: com.mysql.cj.jdbc.Driver

  #关闭spring启动图
  main:
    banner-mode: off

mybatis-plus:
  global-config:
    #关闭mybatis-plus启动图
    banner: false
  configuration:
    #打印log
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    #数据库 下划线命名映射小驼峰命名 user_name --> userName
    map-underscore-to-camel-case: true
~~~

### 代码
~~~java
@Mapper
@DS("second")
public interface XxxMapper extends BaseMapper<Entity> {

}
~~~