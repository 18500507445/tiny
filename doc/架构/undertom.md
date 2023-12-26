## undertom配置

~~~java
/**
 * @author: wzh
 * @description undertow默认连接池
 * @date: 2023/11/02 10:03
 */
@Component
public class UndertowPoolCustomizer implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
            webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(false, 1024));
            deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", webSocketDeploymentInfo);
        });
    }
}
~~~

~~~yml
server:
  # undertow配置
  undertow:
    threads:
      io: 10 #默认机器核数
      worker: 200 #默认 io * 8
~~~