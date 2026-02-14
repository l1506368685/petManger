package com.jsonll.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("stock_check_main")
public class StockCheckMain implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String checkNo;
    private LocalDate checkDate;
    private String status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime confirmTime;
}
