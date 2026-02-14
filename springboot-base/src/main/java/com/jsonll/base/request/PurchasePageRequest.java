package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class PurchasePageRequest extends PageRequest {
    private String purchaseNo;
    private String goodsName;
    private String supplier;
    private LocalDate startDate;
    private LocalDate endDate;
}
