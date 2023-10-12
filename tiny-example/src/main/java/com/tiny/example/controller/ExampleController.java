package com.tiny.example.controller;

import com.tiny.api.pay.client.PayFeignClient;
import com.tiny.common.core.result.BaseController;
import com.tiny.common.core.result.RespResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wzh
 * @description 样例Controller
 * @date: 2023/08/29 12:41
 */
@Slf4j(topic = "ExampleController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tiny-example/api")
public class ExampleController extends BaseController {

    private final PayFeignClient payFeignClient;

    @RequestMapping(value = "/traceId", method = RequestMethod.GET, name = "测试traceId透传")
    @ResponseBody
    public RespResult traceId() {
        return RespResult.success("Hello tiny spring-cloud");
    }

    @RequestMapping(value = "/testLog", method = RequestMethod.GET, name = "测试异步log")
    @ResponseBody
    public RespResult testLog() {
        for (int i = 0; i < 500000; i++) {
            log.info("这是{}条日志！", i);
        }
        return RespResult.success("测试异步log");
    }

    @RequestMapping(value = "/getPayOrderId", method = RequestMethod.GET)
    public RespResult getPayOrderId() {
        return payFeignClient.getPayOrderId();
    }
}
