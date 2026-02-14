package com.jsonll.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("card_type")
public class CardType implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String typeName;
    /** 1储值卡 2次卡 */
    private Integer cardKind;
    private BigDecimal faceValue;
    private BigDecimal price;
    private Integer validDays;
    private Integer totalTimes;
    private String giftRule;
    private Integer maxSubCards;
    private Integer sort;
    private Integer status;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
