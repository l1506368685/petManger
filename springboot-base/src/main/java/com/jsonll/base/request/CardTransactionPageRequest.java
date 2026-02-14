package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class CardTransactionPageRequest extends PageRequest {
    private Long cardId;
    private Long memberId;
    private String transType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
