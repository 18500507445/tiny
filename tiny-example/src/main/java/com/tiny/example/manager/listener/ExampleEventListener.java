package com.tiny.example.manager.listener;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson2.JSONObject;
import com.tiny.example.config.RabbitConfig;
import com.tiny.example.enums.ExampleEventEnum;
import com.tiny.example.manager.bean.ExampleEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description 样例时间监听
 * @date: 2023/12/25 13:17
 */
@Slf4j(topic = "ExampleEventListener")
@Component
@RequiredArgsConstructor
public class ExampleEventListener {

    private final RabbitTemplate rabbitTemplate;

    /**
     * one-事件监听
     * 备注：Async使用名称，会自动注入线程池，如果不写那就是默认的Thread
     * 下面我的用的是getAsyncExecutor线程池，我就配置1个核心，1最大，多次调用线程id都是一个，哈哈
     * {@link ExampleEventEnum#ONE}
     */
    @Async("getAsyncExecutor")
    @EventListener(condition = "#exampleEvent.exampleEventEnum.id == 1")
    public void oneEvent(ExampleEvent<ExampleEvent.MessageDO> exampleEvent) {
        TimeInterval timer = DateUtil.timer();
        ExampleEvent.MessageDO messageDO = exampleEvent.getT();
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_DIRECT, RabbitConfig.ROUTING_KEY, messageDO);
        } finally {
            log.error("事件：{}，oneEvent：{}，耗时：{} ms", exampleEvent.getExampleEventEnum().getName(), JSONObject.toJSONString(messageDO), timer.interval());
        }
    }

}
