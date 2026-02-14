package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DictItemPageRequest extends PageRequest {
    private String dictType;
    private String itemLabel;
}
