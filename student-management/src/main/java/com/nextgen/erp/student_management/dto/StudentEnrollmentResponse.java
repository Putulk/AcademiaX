package com.nextgen.erp.student_management.dto;

import com.nextgen.erp.student_management.enums.StudentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class StudentEnrollmentResponse {

    private UUID id;
    private UUID studentId;
    private UUID academicYearId;
    private UUID classSectionId;
    private String rollNumber;
    private StudentStatus status;
    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
