## 日志使用和书写规范

### 性能对比
#### log框架测试（类比50w数据），spring默认使用logback，性能差异有，但是不是很大，所以还是用logback
|   日志    |  同步   |  异步  |
|:-------:|:-----:|:----:|
| logback | 26.7s | 3.7s |
| log4j2 | 23.4s | 1.5s |

### 使用规范
logback，spring默认集成。命名规范：logback-spring.xml最后加载的写法，保证在读取yml之后再读取，支持springProperty写法
例如：读取yml里面log路径设置设下文（默认值logging.file.path）
~~~xml
<springProperty scope="context" name="logPath" source="logging.file.path" defaultValue="logging.file.path"/>
~~~

### logback如何开启异步
~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- 异步日志输出 -->
    <appender name="async_info" class="ch.qos.logback.classic.AsyncAppender">
        <!-- 不丢失日志.默认的,如果队列的80%已满,则会丢弃TRACT、DEBUG、INFO级别的日志 -->
        <discardingThreshold>0</discardingThreshold>
        <!-- 更改默认的队列的深度,该值会影响性能.默认值为256 -->
        <queueSize>256</queueSize>
        <!-- 添加附加的appender,最多只能添加一个 -->
        <appender-ref ref="file_info" />
    </appender>

    <root level="info">
        <appender-ref ref="async_info"/>
    </root>
</configuration>
~~~

### 当前日志打印策略
* 系统info采取异步打印，提高性能
* error和warn采取同步打印，开启`方法`和`行号`
* debug没有进行配置，直接丢弃，不进行打印


### 全局异常捕获，方法中抛出运行异常GlobalExceptionAdvice进行捕获
如果需要打印error日志，需要手动开启，最好在启动类进行初始化
~~~java
GlobalExceptionAdvice.setRuntimeLog(true);
log.warn("【example】模块，开启GlobalExceptionAdvice ==> RuntimeException errorLog");
~~~


~~~java
throw new RuntimeException("当前sku没有查询到数据");
~~~

~~~json
{
    "bizCode": "400000",
    "bizMessage": "当前sku没有查询到数据",
    "data": null,
    "success": false,
    "traceId": "1734133999497736192",
    "systemTime": 1702284738828,
    "env": "local",
    "ip": "250"
}
~~~


