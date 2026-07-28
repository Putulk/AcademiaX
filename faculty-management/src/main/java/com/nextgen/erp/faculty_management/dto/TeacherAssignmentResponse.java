package com.nextgen.erp.faculty_management.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class TeacherAssignmentResponse {

    private UUID id;

    private UUID teacherId;

    private UUID classSectionId;

    private UUID subjectId;

    private UUID academicYearId;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}