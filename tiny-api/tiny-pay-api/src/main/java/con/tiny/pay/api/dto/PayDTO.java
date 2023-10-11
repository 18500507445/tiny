package con.tiny.pay.api.dto;

import lombok.Data;

/**
 * @author: wzh
 * @description 支付订单DTO
 * @date: 2023/10/11 19:47
 */
@Data
public class PayDTO {

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 支付流水id
     */
    private String transactionId;
}
