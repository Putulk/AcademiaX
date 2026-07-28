package com.nextgen.erp.student_management.dto;

import com.nextgen.erp.student_management.enums.StudentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class StudentEnrollmentRequest {

    @NotNull
    private UUID studentId;

    @NotNull
    private UUID academicYearId;

    @NotNull
    private UUID classSectionId;

    @NotBlank
    private String rollNumber;

    private StudentStatus status;

    private Boolean active;
}
