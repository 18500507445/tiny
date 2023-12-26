## openfeign

### 配置
~~~yaml
feign:
  client:
    config:
      default:
        loggerLevel: NONE
        connectTimeout: 5000
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