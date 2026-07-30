package com.nextgen.erp.examination_management.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record ExamScheduleRequest(

        @NotNull(message = "Exam ID is required")
        UUID examId,

        @NotNull(message = "Subject ID is required")
        UUID subjectId,

        @NotNull(message = "Teacher ID is required")
        UUID teacherId,

        @NotNull(message = "Class ID is required")
        UUID classId,

        @NotNull(message = "Section ID is required")
        UUID sectionId,

        @NotNull(message = "Exam date is required")
        LocalDate examDate,

        @NotNull(message = "Start time is required")
        LocalTime startTime,

        @NotNull(message = "End time is required")
        LocalTime endTime,

        @Size(max = 50, message = "Room number must not exceed 50 characters")
        String roomNumber,

        @NotNull(message = "Maximum marks are required")
        @Min(value = 1, message = "Maximum marks must be greater than 0")
        Integer maxMarks
) {
}