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
@TableName("vaccine_record")
public class VaccineRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long petId;
    private String petName;
    private String vaccineName;
    private String batchNo;
    private LocalDate vaccineDate;
    private String dose;
    private String part;
    private String doctor;
    private BigDecimal fee;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
