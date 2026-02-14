package com.jsonll.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("recharge_record")
public class RechargeRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long memberId;
    private String memberName;
    private String phone;
    private BigDecimal amount;
    private BigDecimal payAmount;
    /** 支付方式：现金/微信/支付宝/转账 */
    private String payMethod;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
}
