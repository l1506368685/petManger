package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClaimPageRequest extends PageRequest {
    private String claimNo;
    private String petName;
    private String reason;
    private LocalDate startDate;
    private LocalDate endDate;
}
