package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemberLifecycleWarningPageRequest extends PageRequest {
    /** 预警类型：CHURN / SLEEPING */
    private String warningType;
}
