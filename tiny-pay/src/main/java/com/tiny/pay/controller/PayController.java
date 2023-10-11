package com.tiny.pay.controller;

import com.tiny.common.core.result.BaseController;
import com.tiny.common.core.result.RespResult;
import com.tiny.common.core.utils.common.IdUtils;
import con.tiny.pay.api.client.PayFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/getPayOrderId", method = RequestMethod.GET, name = "获取支付订单流水id")
    @ResponseBody
    public RespResult getPayOrderId() {
        return RespResult.success(IdUtils.fastSimpleUuid());
    }
}
