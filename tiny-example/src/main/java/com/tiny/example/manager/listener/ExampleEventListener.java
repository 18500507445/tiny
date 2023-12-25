package com.tiny.example.manager.listener;

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
     * {@link ExampleEventEnum#ONE}
     */
    @Async
    @EventListener(condition = "#exampleEvent.exampleEventEnum.id == 1")
    public void oneEvent(ExampleEvent<ExampleEvent.MessageDO> exampleEvent) {
        ExampleEvent.MessageDO messageDO = exampleEvent.getT();
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_DIRECT, RabbitConfig.ROUTING_KEY, messageDO);
        } finally {
            log.info("事件，名称：{}，事件：{}", exampleEvent.getExampleEventEnum().getName(), JSONObject.toJSONString(messageDO));
        }
    }

}
