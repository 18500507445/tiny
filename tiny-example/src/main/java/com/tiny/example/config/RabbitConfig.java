package com.tiny.example.config;

import com.tiny.framework.starter.rabbit.CustomMessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

/**
 * @author: wzh
 * @description: rabbit配置类
 * @date: 2023/11/20 10:41
 */
@Configuration
public class RabbitConfig {

    /**
     * 生产者设置
     * （1）添加消息转换器
     */
    @Bean
    @Order(1)
    public RabbitTemplate rabbitTemplate(@Qualifier("primaryConnectionFactory") ConnectionFactory primaryConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(primaryConnectionFactory);
        rabbitTemplate.setMessageConverter(new CustomMessageConverter());
        return rabbitTemplate;
    }

    @Primary
    @Bean
    @Order(2)
    public RabbitAdmin rabbitAdmin(@Qualifier("rabbitTemplate") RabbitTemplate rabbitTemplate) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        //开启自动创建队列
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * 监听者设置
     * （1）添加消息转换器MessageConverter
     */
    @Bean
    @Order(3)
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(@Qualifier("primaryConnectionFactory") ConnectionFactory primaryConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(primaryConnectionFactory);
        factory.setMessageConverter(new CustomMessageConverter());
        factory.setAdviceChain(retries());
        return factory;
    }

    /**
     * 消息重试器
     * @description: 重试完成提示 ==> Retry Policy Exhausted
     */
    @Bean
    public RetryOperationsInterceptor retries() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(3) //设置最大尝试次数为3（不重试）
                .backOffOptions(1000, 3.0, 10000)
                .recoverer(new RejectAndDontRequeueRecoverer()).build();
    }

    //直连交换机(自带)
    public static final String EXCHANGE_DIRECT = "amq.direct";

    public static final String EXAMPLE_QUEUE = "exampleQueue";

    public static final String ROUTING_KEY = "exampleRoutingKey";

    /**
     * 直连交换机
     */
    @Bean
    public Exchange exampleDirectExchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_DIRECT).build();
    }

    /**
     * 队列
     */
    @Bean
    public Queue exampleQueue() {
        return new Queue(EXAMPLE_QUEUE);
    }

    /**
     * 绑定队列到direct交换机
     */
    @Bean
    public Binding exampleBindingProduct() {
        return BindingBuilder
                .bind(exampleQueue())
                .to(exampleDirectExchange())
                .with(ROUTING_KEY).noargs();
    }


}
