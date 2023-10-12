package com.tiny.pay.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import com.tiny.api.order.feign.OrderFeignClient;
import com.tiny.api.pay.client.PayFeignClient;
import com.tiny.common.core.result.BaseController;
import com.tiny.common.core.result.RespResult;
import com.tiny.common.core.utils.common.IdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: wzh
 * @description 支付Controller
 * @date: 2023/10/11 19:40
 */
@Slf4j(topic = "PayController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tiny-pay/api")
public class PayController extends BaseController implements PayFeignClient {

    private final OrderFeignClient orderFeignClient;

    @RequestMapping(value = "/getPayOrderId", method = RequestMethod.GET, name = "获取支付订单流水id")
    @ResponseBody
    public RespResult getPayOrderId() {
        RespResult result = orderFeignClient.getOrderId();
        Map<String, String> map = MapUtil.of("orderId", Convert.toStr(result.get("data")));
        map.put("transactionId", IdUtils.fastSimpleUuid());
        return RespResult.success(map);
    }
}
