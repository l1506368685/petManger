package com.jsonll.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("stock_check_item")
public class StockCheckItem implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long checkId;
    private Long goodsId;
    private String goodsName;
    private String goodsCode;
    private Integer bookQuantity;
    private Integer actualQuantity;
    private Integer diffQuantity;
    private String remark;
    private LocalDateTime createTime;
}
