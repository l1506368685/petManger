package com.jsonll.base.entity;

import lombok.Data;

import java.math.BigDecimal;

/** 会员消费汇总，用于等级计算 */
@Data
public class MemberConsumeSum {
    private Long memberId;
    private BigDecimal totalAmount;
}
