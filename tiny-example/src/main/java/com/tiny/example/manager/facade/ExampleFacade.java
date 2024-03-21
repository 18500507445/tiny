package com.tiny.example.manager.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description: 门面类，通用逻辑处理层
 * （1）第三方平台进行接口封装
 * （2）Service层通用能力的下沉，如缓存方案、中间件通用处理，与DAO层交互，对多个DAO的组合复用
 * （3）被驱动器MQ
 * （4）被驱动器Redis
 * （5）远程调用、rpc
 * @date: 2023/11/29 15:41
 */
@Slf4j(topic = "ExampleManager")
@Component
@RequiredArgsConstructor
public class ExampleFacade {

}
