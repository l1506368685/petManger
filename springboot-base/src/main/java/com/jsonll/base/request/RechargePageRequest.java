package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class RechargePageRequest extends PageRequest {
    private String orderNo;
    private String memberName;
    private String phone;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
