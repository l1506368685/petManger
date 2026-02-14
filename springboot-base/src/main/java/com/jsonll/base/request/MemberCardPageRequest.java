package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemberCardPageRequest extends PageRequest {
    private String cardNo;
    private Long memberId;
    private String memberName;
    private Long cardTypeId;
    private Integer cardKind;
    private Integer status;
}
