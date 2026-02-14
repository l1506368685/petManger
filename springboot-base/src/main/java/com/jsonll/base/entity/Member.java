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
@TableName("member")
public class Member implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String memberNo;
    private String name;
    private String phone;
    private BigDecimal balance;
    /** 最后消费时间（订单支付/完成时更新） */
    private LocalDateTime lastConsumeTime;
    /** 标签 JSON 数组，如 ["沉睡","高价值"] */
    private String tags;
    private String level;
    private String gender;
    private LocalDate birthday;
    private String address;
    private String remark;
    private Integer status;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
