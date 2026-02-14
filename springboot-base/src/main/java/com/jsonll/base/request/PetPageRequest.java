package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PetPageRequest extends PageRequest {
    private String petName;
    private String contactName;
    private String contactPhone;
}
