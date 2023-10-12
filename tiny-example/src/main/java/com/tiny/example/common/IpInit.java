package com.tiny.example.common;

import com.tiny.common.core.result.RespResult;
import com.tiny.common.core.utils.common.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: wzh
 * @description 当前机器ip初始化
 * @date: 2023/10/12 11:41
 */
@Component
@Slf4j(topic = "IpInit")
public class IpInit {

    public static String INTERNET_IP = "00";

    @PostConstruct
    void init() {
        INTERNET_IP = IpUtils.getInternetIp("curl cip.cc");
        log.warn("获取公网ip初始化后几位完成");
        RespResult.IP = INTERNET_IP;
    }
}
