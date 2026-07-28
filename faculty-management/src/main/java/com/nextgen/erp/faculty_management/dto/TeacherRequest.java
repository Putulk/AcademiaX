package com.nextgen.erp.faculty_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class TeacherRequest {

    @NotNull
    private UUID userProfileId;

    @NotBlank
    private String employeeCode;

    @NotBlank
    private String designation;

    @NotBlank
    private String department;

    @NotNull
    private LocalDate joiningDate;

    private Integer experienceYears;

    private BigDecimal salary;

    private Boolean active;
}
