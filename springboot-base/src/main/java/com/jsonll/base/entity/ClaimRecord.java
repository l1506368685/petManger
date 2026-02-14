package com.jsonll.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("claim_record")
public class ClaimRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String claimNo;
    private Long petId;
    private String petName;
    private LocalDate eventDate;
    private BigDecimal claimAmount;
    private BigDecimal finalAmount;
    private String payMethod;
    private LocalDateTime payTime;
    private String reason;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
