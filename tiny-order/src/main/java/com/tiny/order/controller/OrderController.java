package com.tiny.order.controller;

import com.tiny.common.core.result.BaseController;
import com.tiny.common.core.result.RespResult;
import com.tiny.common.core.utils.common.IdUtils;
import com.tiny.order.api.feign.OrderFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wzh
 * @description 订单Controller
 * @date: 2023/10/11 19:38
 */
@Slf4j(topic = "OrderController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tiny-order/api")
public class OrderController extends BaseController implements OrderFeignClient {

    @RequestMapping(value = "/getOrderId", method = RequestMethod.GET, name = "获取订单id")
    @ResponseBody
    public RespResult getOrderId() {
        return RespResult.success(IdUtils.fastSimpleUuid());
    }
}
