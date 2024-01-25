package com.tiny.framework.starter.rabbit;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.tiny.framework.core.trace.Trace;
import com.tiny.framework.core.trace.TraceContext;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description: 自定义消息转换器 + 透传TraceId
 * @date: 2023/11/30 11:18
 */
@Component
@SuppressWarnings("NullableProblems")
public class CustomMessageConverter implements MessageConverter {

    /**
     * 消息转换
     * 通过fastJson的parse方法进行分别处理，防止传json再转一次json导致message.body获取内容解析报错
     *
     * @param o                 the object to convert
     * @param messageProperties The message properties.
     * @return
     * @throws MessageConversionException
     */

    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        //获取MDC中的traceId，没有就生成一个
        String traceId = TraceContext.getTraceId();
        if (StrUtil.isEmpty(traceId)) {
            traceId = TraceContext.getCurrentTrace().getTraceId();
        }
        messageProperties.setHeader(Trace.TRACE_ID, traceId);
        try {
            //转javaBean对象
            Object parse = JSON.parse(Convert.toStr(o));
            return new Message(JSON.toJSONBytes(parse), messageProperties);
        } catch (Exception e) {
            //转message对象
            return new Message(JSON.toJSONBytes(o), messageProperties);
        }
    }

    /**
     * 根据消息类型，解析成<T>，根据message.getMessageProperties().getInferredArgumentType()进行类型推断 </>
     * 支持的有（1）jsonString转Message对象（2）对象转对象
     *
     * @param message the message to convert
     * @throws MessageConversionException
     */
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        MessageProperties messageProperties = message.getMessageProperties();
        TraceContext.setCurrentTrace(messageProperties.getHeader(Trace.TRACE_ID));
        return JSON.parseObject(message.getBody(), messageProperties.getInferredArgumentType());
    }
}
