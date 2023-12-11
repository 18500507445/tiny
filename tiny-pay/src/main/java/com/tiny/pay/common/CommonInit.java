package com.tiny.pay.common;

import com.tiny.common.core.result.RespResult;
import com.tiny.common.core.utils.common.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: wzh
 * @description 通用初始化类
 * @date: 2023/10/12 11:41
 */
@Component
@Slf4j(topic = "IpInit")
public class CommonInit {

    public static String INTERNET_IP = "00";

    /**
     * 初始化ip
     */
    @PostConstruct
    void initIp() {
        INTERNET_IP = IpUtils.getInternetIp("curl cip.cc");
        log.warn("获取公网ip后2位，初始化RespResult.IP");
        RespResult.setIp(INTERNET_IP);
    }

}
