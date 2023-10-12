package com.tiny.api.pay.vo;

import lombok.Data;

/**
 * @author: wzh
 * @description 支付订单VO
 * @date: 2023/10/11 19:47
 */
@Data
public class PayVO {

    private String orderId;

    private String transactionId;
}
