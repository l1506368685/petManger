package com.jsonll.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("member_lifecycle_warning")
public class MemberLifecycleWarning implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long memberId;
    private String memberName;
    private String phone;
    /** 预警类型：CHURN 流失预警 / SLEEPING 沉睡 */
    private String warningType;
    private LocalDateTime lastConsumeTime;
    private Integer registerDays;
    private LocalDateTime createTime;
}
