package com.jsonll.base.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardBuyDTO {
    private Long memberId;
    private Long cardTypeId;
    private BigDecimal payAmount;
    private String remark;
}
