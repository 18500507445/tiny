package com.tiny.example.controller;

import com.tiny.common.web.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wzh
 * @description 样例Controller
 * @date: 2023/08/29 12:41
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tiny-example/api")
public class ExampleController extends BaseController {

    @RequestMapping(value = "/test" , method = RequestMethod.GET, name = "测试")
    String test() {
        return "Hello tiny spring-cloud" ;
    }
}
