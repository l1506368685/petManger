package com.jsonll.base.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardConsumeDTO {
    private Long memberId;
    /** 消费总金额 */
    private BigDecimal amount;
    /** 本次使用次卡次数（可选，不传则按金额优先扣次卡再扣储值） */
    private Integer useTimes;
    private String remark;
    private String bizOrderNo;
}
