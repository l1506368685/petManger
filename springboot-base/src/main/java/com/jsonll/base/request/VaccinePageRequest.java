package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class VaccinePageRequest extends PageRequest {
    private String vaccineName;
    private String petName;
    private String doctor;
    private LocalDate startDate;
    private LocalDate endDate;
}
