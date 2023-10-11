package con.tiny.pay.api.client;

import com.tiny.common.core.result.RespResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: wzh
 * @description 支付订单feign
 * @date: 2023/10/11 19:46
 */
@FeignClient(value = "tiny-pay")
public interface PayFeignClient {

    @RequestMapping(value = "/getPayOrderId", method = RequestMethod.GET, name = "获取支付订单流水id")
    RespResult getPayOrderId();
}
