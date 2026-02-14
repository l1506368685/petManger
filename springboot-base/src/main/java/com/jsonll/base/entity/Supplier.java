package com.jsonll.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("supplier")
public class Supplier implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 供应商名称 */
    private String name;
    /** 联系人 */
    private String contact;
    /** 联系电话 */
    private String phone;
    /** 地址 */
    private String address;
    private String remark;
    /** 状态 0禁用 1启用 */
    private Integer status;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
