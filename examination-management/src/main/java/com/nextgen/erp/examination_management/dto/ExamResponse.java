package com.nextgen.erp.examination_management.dto;

import com.nextgen.erp.examination_management.enums.ExamStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponse {

    private UUID id;

    private String name;

    private String academicYear;

    private UUID classId;

    private LocalDate startDate;

    private LocalDate endDate;

    private ExamStatus status;

    private String description;
}