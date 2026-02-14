package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class MedicalPageRequest extends PageRequest {
    private String petName;
    private String doctor;
    private String symptom;
    private LocalDate startDate;
    private LocalDate endDate;
}
