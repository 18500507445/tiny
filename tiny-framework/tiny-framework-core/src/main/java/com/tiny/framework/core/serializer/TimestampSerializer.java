package com.tiny.framework.core.serializer;

import cn.hutool.core.convert.Convert;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @author: wzh
 * @description: jackson毫秒序列化
 * @date: 2024/01/12 17:17
 */
public class TimestampSerializer<T> extends JsonSerializer<T> {

    /**
     * @param t                  t
     * @param jsonGenerator      jsonGenerator
     * @param serializerProvider serializerProvider
     * @description: 如何去用：jackson的注解，@JsonSerialize(using = TimestampSerializer.class)
     */
    @Override
    public void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        long timestamp = 0;
        // 将时间转换为时间戳
        if (t instanceof Date) {
            Date date = Convert.convert(Date.class, t);
            timestamp = date.getTime();
        } else if (t instanceof LocalDateTime) {
            LocalDateTime localDateTime = Convert.convert(LocalDateTime.class, t);
            timestamp = localDateTime.toEpochSecond(ZoneOffset.UTC);
        }
        // 将时间戳写入JSON
        jsonGenerator.writeNumber(timestamp);
    }
}