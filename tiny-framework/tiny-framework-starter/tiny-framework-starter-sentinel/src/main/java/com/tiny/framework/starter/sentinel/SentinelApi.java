package com.tiny.framework.starter.sentinel;

import com.tiny.framework.core.exception.BusinessException;
import com.tiny.framework.core.result.base.ResResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wzh
 * @description: sentinel接口
 * @date: 2024/11/25 14:13
 */
@RestController
public interface SentinelApi {

    @RequestMapping("/sentinel")
    default ResResult<Void> sentinel() {
        throw new BusinessException("您的请求频率过快，请稍后再试！");
    }
}
