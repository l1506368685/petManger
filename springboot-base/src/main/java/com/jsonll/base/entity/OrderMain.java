package com.jsonll.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_main")
public class OrderMain implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long memberId;
    private String memberName;
    private Long petId;
    private String petName;
    private BigDecimal totalAmount;
    private String status;
    /** 支付方式：现金/微信/支付宝/转账 */
    private String payMethod;
    private LocalDateTime orderTime;
    /** 订单商品总数量（明细数量之和，仅查询列表时填充，不落库） */
    @TableField(exist = false)
    private Integer totalQuantity;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
