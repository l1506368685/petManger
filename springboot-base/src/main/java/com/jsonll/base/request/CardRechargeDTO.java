package com.jsonll.base.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardRechargeDTO {
    private Long cardId;
    private BigDecimal amount;
    private BigDecimal payAmount;
    private String remark;
}
