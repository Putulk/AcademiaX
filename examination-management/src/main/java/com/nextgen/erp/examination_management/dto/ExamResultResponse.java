package com.nextgen.erp.examination_management.dto;

import com.nextgen.erp.examination_management.enums.Grade;
import com.nextgen.erp.examination_management.enums.ResultStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultResponse {

    private UUID id;

    private UUID examId;

    private UUID studentEnrollmentId;

    private UUID subjectId;

    private Double marksObtained;

    private Integer maxMarks;

    private Double percentage;

    private Grade grade;

    private ResultStatus resultStatus;

    private Boolean absent;
}
