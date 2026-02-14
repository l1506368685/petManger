package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class InventoryFlowPageRequest extends PageRequest {
    private String goodsName;
    private String flowType;
    /** 业务类型：正常入库、正常出库、退库 */
    private String bizType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
