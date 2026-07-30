package com.nextgen.erp.examination_management.dto;

import com.nextgen.erp.examination_management.enums.ExamStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record ExamRequest(

        @NotBlank(message = "Exam name is required")
        @Size(max = 100, message = "Exam name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Academic year is required")
        String academicYear,

        @NotNull(message = "Class ID is required")
        UUID classId,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        LocalDate endDate,

        ExamStatus status,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description
) {
}