package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CardTypePageRequest extends PageRequest {
    private String typeName;
    private Integer cardKind;
    private Integer status;
}
