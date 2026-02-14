package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class StockCheckPageRequest extends PageRequest {
    private String checkNo;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
}
