package com.jsonll.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("card_transaction")
public class CardTransaction implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long cardId;
    private Long memberId;
    private String memberName;
    private String transType;
    private BigDecimal amount;
    private Integer timesBefore;
    private Integer timesChange;
    private Integer timesAfter;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private String remark;
    private String bizOrderNo;
    private Integer deleted;
    private LocalDateTime createTime;
}
