package com.jsonll.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("member_level_rule")
public class MemberLevelRule implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String levelName;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
