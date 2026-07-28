package com.nextgen.erp.faculty_management.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class TeacherResponse {

    private UUID id;

    private UUID userProfileId;

    private String employeeCode;

    private String designation;

    private String department;

    private LocalDate joiningDate;

    private Integer experienceYears;

    private BigDecimal salary;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
