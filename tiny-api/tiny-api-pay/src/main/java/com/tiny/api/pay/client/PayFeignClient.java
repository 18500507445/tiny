package com.tiny.api.pay.client;

import com.tiny.common.core.result.RespResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: wzh
 * @description 支付订单feign
 * @date: 2023/10/11 19:46
 */
//如果不配置url，name就是注册中心的服务名称。配置了url，value就是普通的client名称
//默认的配置类是FeignClientsConfiguration.class
@FeignClient(name = "tiny-pay", path = "tiny-pay/api")
public interface PayFeignClient {

    @RequestMapping(value = "/getPayOrderId", method = RequestMethod.GET, name = "获取支付订单流水id")
    RespResult getPayOrderId();
}
