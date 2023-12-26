package com.tiny.api.order.feign;

import com.tiny.framework.core.result.RespResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: wzh
 * @description order-api-feign
 * @date: 2023/10/11 19:18
 */
@FeignClient(name = "tiny-order", path = "tiny-order/api")
public interface OrderFeignClient {

    @RequestMapping(value = "/getOrderId", method = RequestMethod.GET, name = "获取订单id")
    RespResult getOrderId();

}
