package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SupplierPageRequest extends PageRequest {
    private String name;
    private Integer status;
}
