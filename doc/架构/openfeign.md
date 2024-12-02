## openfeign

### openfeign和loadbalancer搭配实用

### EnableFeignClients注解解释
~~~java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FeignClientsRegistrar.class)
public @interface EnableFeignClients {

	// 扫描@FeignClient包地址
	String[] value() default {};

	// 用户扫描Feign客户端的包，也就是@FeignClient标注的类，与value同义，并且互斥
	String[] basePackages() default {};

	// basePackages()的类型安全替代方案，用于指定要扫描带注释的组件的包。每个指定类别的包将被扫描。 考虑在每个包中创建一个特殊的无操作标记类或接口，除了被该属性引用之外没有其他用途。
	Class<?>[] basePackageClasses() default {};

	// 为所有假客户端定制@Configuration，默认配置都在FeignClientsConfiguration中，可以自己定制
	Class<?>[] defaultConfiguration() default {};

	// 可以指定@FeignClient标注的类，如果不为空，就会禁用类路径扫描
	Class<?>[] clients() default {};
}

~~~

### 配置
~~~yaml
feign:
  client:
    config:
      # 全局默认配置 如果不单独配置每个服务会走默认配置
      default:
        # 日志级别 NONE：不打印  BASIC：打印简单信息 HEADERS：打印头信息 FULL：打印全部信息 （默认 NONE）
        loggerLevel: NONE
        # 连接建立超时时间，默认为10秒
        connectTimeout: 5000
        # 请求处理超时时间，默认为60秒
        readTimeout: 3000

      # 单独设置，提供方的服务名
      product-service-name:
        #请求日志级别  
        loggerLevel: NONE
        contract: feign.Contract.Default #设置为默认的契约（还原成原生注解）
        # 连接超时时间，一般只在发现服务时用到，默认2s，单位：毫秒
        connectTimeout: 5000
        # 请求处理超时时间，默认5s，单位：毫秒
        readTimeout: 3000

      # 远程服务的服务名（单个设置的优先级大于全局配置）
      remote-server-name:

  httpclient:
    enabled: ture # 开启feign对HttpClient的支持
    max-connections: 200 # 最大的连接数
    max-connections-per-route: 50 # 每个路径的最大连接数

  # okhttp
  okhttp:
    enabled: false 
~~~