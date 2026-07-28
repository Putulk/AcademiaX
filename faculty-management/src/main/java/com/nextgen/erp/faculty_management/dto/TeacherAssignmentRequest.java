package com.nextgen.erp.faculty_management.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TeacherAssignmentRequest {

    @NotNull
    private UUID teacherId;

    @NotNull
    private UUID classSectionId;

    @NotNull
    private UUID subjectId;

    @NotNull
    private UUID academicYearId;

    private Boolean active;
}
