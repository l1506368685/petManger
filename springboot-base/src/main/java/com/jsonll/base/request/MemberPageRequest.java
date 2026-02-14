package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemberPageRequest extends PageRequest {
    private String name;
    private String phone;
    private String memberNo;
}
