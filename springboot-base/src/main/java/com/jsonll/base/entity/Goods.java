package com.jsonll.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("goods")
public class Goods implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String goodsName;
    private String goodsCode;
    private String brand;
    private BigDecimal price;
    /** 成本价，用于毛利分析 */
    private BigDecimal costPrice;
    private BigDecimal originalPrice;
    private Integer stock;
    /** 库存下限，低于时产生采购预警 */
    private Integer stockLower;
    /** 库存上限，高于时产生库存积压预警 */
    private Integer stockUpper;
    private String intro;
    private String unit;
    private String category;
    private Integer status;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
