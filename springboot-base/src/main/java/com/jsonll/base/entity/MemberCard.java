package com.jsonll.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("member_card")
public class MemberCard implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String cardNo;
    private Long memberId;
    private String memberName;
    private Long cardTypeId;
    private String cardTypeName;
    /** 1储值卡 2次卡 */
    private Integer cardKind;
    private BigDecimal balance;
    private Integer totalTimes;
    private Integer remainTimes;
    private LocalDateTime expireTime;
    /** 主卡ID，副卡时非空 */
    private Long mainCardId;
    private Integer status;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
