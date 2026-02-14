package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderPageRequest extends PageRequest {
    private String orderNo;
    private String petName;
    private String memberName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}
