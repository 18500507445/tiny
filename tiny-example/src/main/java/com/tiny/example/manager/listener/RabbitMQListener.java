package com.tiny.example.manager.listener;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.tiny.api.pay.client.PayFeignClient;
import com.tiny.example.config.RabbitConfig;
import com.tiny.example.manager.bean.ExampleEvent;
import com.tiny.framework.core.result.RespResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: wzh
 * @description: rabbitMQ监听
 * @date: 2023/12/25 13:18
 */
@Component
@Slf4j(topic = "RabbitMQListener")
@RequiredArgsConstructor
public class RabbitMQListener {

    private final PayFeignClient payFeignClient;

    /**
     * 直连模式
     *
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConfig.EXAMPLE_QUEUE))
    public void one(Message message, ExampleEvent.MessageDO messageDO) {
        long tag = message.getMessageProperties().getDeliveryTag();
        log.error("直连模式one，消息id：{}，消息内容：{}，messageDO：{}", tag, JSONUtil.toJsonStr(new String(message.getBody())), JSONUtil.toJsonStr(messageDO));
        RespResult result = payFeignClient.getPayOrderId();
        Map<String, Object> hashMap = MapUtil.of("payOrder", result.get("data"));
        log.error("userInfo:" + JSONUtil.toJsonStr(hashMap));
    }
}
