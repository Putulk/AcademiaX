package com.nextgen.erp.examination_management.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamScheduleResponse {

    private UUID id;

    private UUID examId;

    private UUID subjectId;

    private UUID teacherId;

    private UUID classId;

    private UUID sectionId;

    private LocalDate examDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String roomNumber;

    private Integer maxMarks;
}