package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminPageRequest extends PageRequest {
    private String username;
    private String name;
    private String role;
}
