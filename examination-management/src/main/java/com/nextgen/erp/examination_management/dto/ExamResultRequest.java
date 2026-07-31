package com.nextgen.erp.examination_management.dto;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record ExamResultRequest(

        @NotNull(message = "Exam ID is required")
        UUID examId,

        @NotNull(message = "Student enrollment ID is required")
        UUID studentEnrollmentId,

        @NotNull(message = "Subject ID is required")
        UUID subjectId,

        @NotNull(message = "Marks obtained is required")
        @DecimalMin(value = "0.0", message = "Marks obtained cannot be negative")
        Double marksObtained,

        @NotNull(message = "Maximum marks are required")
        @Min(value = 1, message = "Maximum marks must be greater than 0")
        Integer maxMarks,

        boolean absent
) {
}
