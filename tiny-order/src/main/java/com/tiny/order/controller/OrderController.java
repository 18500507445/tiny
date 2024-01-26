package com.tiny.order.controller;

import com.tiny.api.order.feign.OrderFeignClient;
import com.tiny.framework.core.result.BaseController;
import com.tiny.framework.core.result.ResResult;
import com.tiny.framework.core.utils.common.IdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wzh
 * @description: 订单Controller
 * @date: 2023/10/11 19:38
 */
@Slf4j(topic = "OrderController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tiny-order/api")
public class OrderController extends BaseController implements OrderFeignClient {

    @RequestMapping(value = "/getOrderId", method = RequestMethod.GET, name = "获取订单id")
    @ResponseBody
    public ResResult<String> getOrderId() {
        return ResResult.success(IdUtils.fastSimpleUuid());
    }
}
