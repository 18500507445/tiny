package com.tiny.example.config;

import com.tiny.common.starter.rabbit.CustomMessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wzh
 * @description rabbit配置类
 * @date: 2023/11/20 10:41
 */
@Configuration
@EnableRabbit
public class RabbitConfig {

    /**
     * 生产者设置
     * （1）添加消息转换器
     */
    @Bean
    public RabbitTemplate rabbitTemplate(@Qualifier("primaryConnectionFactory") ConnectionFactory primaryConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(primaryConnectionFactory);
        rabbitTemplate.setMessageConverter(new CustomMessageConverter());
        return rabbitTemplate;
    }

    /**
     * 监听者设置
     * （1）添加消息转换器MessageConverter
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(@Qualifier("primaryConnectionFactory") ConnectionFactory primaryConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(primaryConnectionFactory);
        factory.setMessageConverter(new CustomMessageConverter());
        return factory;
    }

    /**
     * topic交换机
     */
    @Bean
    public Exchange exampleTopicExchange() {
        return ExchangeBuilder.topicExchange("amq.topic").build();
    }

    /**
     * 队列
     */
    @Bean
    public Queue exampleQueue() {
        return new Queue("exampleQueue");
    }

    /**
     * 绑定队列到topic交换机
     */
    @Bean
    public Binding exampleBindingProduct() {
        return BindingBuilder.bind(exampleQueue()).to(exampleTopicExchange()).with("exampleRoutingKey").noargs();
    }


}
