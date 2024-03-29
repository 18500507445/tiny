## mybatis-plus配置

### mybatis-plus动态数据源
~~~yaml
##德鲁伊连接池
spring:
  datasource:
    dynamic:
      #主数据源
      primary: master
      strict: false
      datasource:
        master:
          username: username
          password: password
          url: jdbc:mysql://xx.xxx.xxx.xxx:3306/study?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
          driver-class-name: com.mysql.cj.jdbc.Driver
          #设置连接池参数
          druid:
            initialSize: 5
            minIdle: 5
            maxActive: 20
            maxWait: 6000
            timeBetweenEvictionRunsMillis: 60000
            minEvictableIdleTimeMillis: 300000
            validationQuery: SELECT 1 FROM DUAL
            testWhileIdle: true
            testOnBorrow: false
            testOnReturn: false
            poolPreparedStatements: true
            maxPoolPreparedStatementPerConnectionSize: 20
            filters: stat,wall
            connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
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

### 多数据源Mapper注解
~~~java
@Mapper
@DS("db1")
public interface XxxMapper extends BaseMapper<Entity> {

}
~~~


### mybatis-plus内置分页插件（需要配置，放到avic-parent里面去配置，自动装配），如果已经添加了pagehelper依赖，`不建议这么做`
~~~java
@Configuration
public class MybatisPlusConfig {
   
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        //MybatisPlusInterceptor插件，默认提供分页插件，如需其他MP内置插件，则需自定义该Bean
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        //分页插件 用依赖包自己的Page对象，不是pagehelper的Page
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }


}
~~~

### 内置分页如何使用
~~~java
Page<UserDO> page = mapper.selectPage(new Page<>(1, 5), queryWrapper);
~~~

### 如何代码生成
在需要生成的模块下创建测试类，参考avic-example模块test类`ExampleGeneratorTest`，执行mybatisPlusGeneratorTest方法既可。