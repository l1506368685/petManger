package com.jsonll.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("pet")
public class Pet implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String petName;
    private Long typeId;
    private String typeName;
    private String breed;
    private String color;
    private String gender;
    private LocalDate birthday;
    private String contactName;
    private String contactPhone;
    private Long memberId;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
