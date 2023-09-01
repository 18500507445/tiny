package com.tiny.example.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.tiny.common.core.entity.RespResult;
import com.tiny.common.web.BaseController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wzh
 * @description 样例Controller
 * @date: 2023/08/29 12:41
 */
@Slf4j(topic = "ExampleController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tiny-example/api")
public class ExampleController extends BaseController {

    @RequestMapping(value = "/test", method = RequestMethod.GET, name = "测试")
    @ResponseBody
    public RespResult test() {
        TimeInterval timer = DateUtil.timer();
        for (int i = 0; i < 500000; i++) {
            log.info("这是{}条日志！", i);
        }
        log.info("当前耗时：{}ms", timer.interval());
        return RespResult.success("Hello tiny spring-cloud");
    }
}
