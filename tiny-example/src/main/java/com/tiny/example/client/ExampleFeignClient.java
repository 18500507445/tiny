package com.tiny.example.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: wzh
 * @description: 样例feign接口
 * @date: 2023/11/29 15:50
 */
@FeignClient(name = "example", url = "http://localhost:8080/avic-example/api-c")
public class ExampleFeignClient {

}
