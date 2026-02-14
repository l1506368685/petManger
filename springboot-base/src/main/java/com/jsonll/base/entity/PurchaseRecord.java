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
@TableName("purchase_record")
public class PurchaseRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String purchaseNo;
    private Long goodsId;
    private String goodsName;
    private String spec;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal amount;
    private String supplier;
    private LocalDate purchaseDate;
    /** 批次号（用于保质期追溯） */
    private String batchNo;
    /** 生产日期 */
    private LocalDate productionDate;
    /** 到期日/保质期截止日，用于保质期预警 */
    private LocalDate expiryDate;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
