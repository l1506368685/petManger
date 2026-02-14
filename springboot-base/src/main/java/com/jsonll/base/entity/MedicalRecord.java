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
@TableName("medical_record")
public class MedicalRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long petId;
    private String petName;
    private LocalDate visitDate;
    private String symptom;
    private String diagnosisInitial;
    private String diagnosisFinal;
    private String doctor;
    private String prescription;
    private BigDecimal fee;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
