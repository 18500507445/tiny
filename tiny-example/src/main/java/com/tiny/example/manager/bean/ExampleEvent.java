package com.tiny.example.manager.bean;

import com.tiny.example.enums.ExampleEventEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.data.annotation.Id;

/**
 * @author: wzh
 * @description 渠道事件通用类
 * @date: 2023/11/21 17:03
 */
@Getter
@Setter
public class ExampleEvent<T> extends ApplicationEvent {

    private ExampleEventEnum exampleEventEnum;

    private T t;

    public ExampleEvent(ExampleEventEnum exampleEventEnum, T t) {
        super(t);
        this.exampleEventEnum = exampleEventEnum;
        this.t = t;
    }

    /**
     * 事件实体类
     * {@link ExampleEventEnum#ONE}
     */
    @Data
    @Builder
    public static class MessageDO {

        @Id
        private String id;

        //消息
        private String message;
    }

}
