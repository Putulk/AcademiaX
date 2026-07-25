package com.nextgen.erp.user_management.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class StudentResponse {

    private UUID id;

    private UUID userProfileId;

    private String admissionNumber;

    private String rollNumber;

    private String academicYear;

    private LocalDate admissionDate;

    private String bloodGroup;

    private String religion;

    private String category;

    private String house;

    private Boolean active;
}
