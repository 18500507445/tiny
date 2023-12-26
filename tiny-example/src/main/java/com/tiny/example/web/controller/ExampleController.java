package com.tiny.example.web.controller;

import cn.hutool.core.map.MapUtil;
import com.tiny.api.pay.client.PayFeignClient;
import com.tiny.framework.core.result.BaseController;
import com.tiny.framework.core.result.RespResult;
import com.tiny.framework.core.thread.ThreadWrap;
import com.tiny.framework.core.user.UserContext;
import com.tiny.example.enums.ExampleEventEnum;
import com.tiny.example.manager.bean.ExampleEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: wzh
 * @description 样例Controller
 * <p>
 * todo「/tiny-example/」可以放到配置文件中设置：server.servlet.context-path:/tiny-example/
 * @date: 2023/08/29 12:41
 */
@Slf4j(topic = "ExampleController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tiny-example/api")
public class ExampleController extends BaseController {

    private final PayFeignClient payFeignClient;

    private final ApplicationEventPublisher applicationEventPublisher;

    @RequestMapping(value = "/traceId", method = RequestMethod.GET, name = "测试traceId透传")
    public RespResult traceId() {
        return RespResult.success("Hello tiny spring-cloud");
    }

    @RequestMapping(value = "/testLog", method = RequestMethod.GET, name = "测试异步log")
    public RespResult testLog() {
        for (int i = 0; i < 500000; i++) {
            log.info("这是{}条日志！", i);
        }
        return RespResult.success("测试异步log");
    }

    @RequestMapping(value = "/getPayOrderId", method = RequestMethod.GET)
    public RespResult getPayOrderId() {
        UserContext.UserToken userToken = UserContext.get();
        RespResult result = payFeignClient.getPayOrderId();
        Map<String, Object> hashMap = MapUtil.of("payOrder", result.get("data"));
        hashMap.put("userInfo", userToken);
        return RespResult.success(hashMap);
    }


    /**
     * 终极traceId链路测试
     * 普通线程 --> 事件（spring-async自定义线程）--> rabbitMQ --> openFeign
     */
    @RequestMapping(value = "/finalTraceId", method = RequestMethod.GET, name = "测试traceId透传")
    public RespResult finalTraceId() {
        log.info("finalTraceId-start");

        //1. 首先开启一个普通线程
        new Thread(ThreadWrap.runnableWrap(() -> {
            log.info("finalTraceId-普通线程");

            //2. 发送spring事件
            ExampleEvent.MessageDO messageDO = ExampleEvent.MessageDO.builder()
                    .id("1")
                    .message("通过spring-event-async解耦")
                    .build();

            applicationEventPublisher.publishEvent(new ExampleEvent<>(ExampleEventEnum.ONE, messageDO));
        })).start();

        return RespResult.success("finalTraceId");
    }

}
