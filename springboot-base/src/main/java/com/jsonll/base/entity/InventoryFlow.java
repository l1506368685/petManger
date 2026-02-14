package com.jsonll.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("inventory_flow")
public class InventoryFlow implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String flowNo;
    private Long goodsId;
    private String goodsName;
    private String flowType;
    /** 业务类型：正常入库、正常出库、退库等，来自数据字典 inventory_biz_type */
    private String bizType;
    private BigDecimal quantity;
    private BigDecimal afterQuantity;
    private String refNo;
    private LocalDateTime flowTime;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
}
