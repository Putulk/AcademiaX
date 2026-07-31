package com.nextgen.erp.examination_management.mapper;

import com.nextgen.erp.examination_management.dto.ExamResultRequest;
import com.nextgen.erp.examination_management.dto.ExamResultResponse;
import com.nextgen.erp.examination_management.entity.ExamResult;
import org.springframework.stereotype.Component;

@Component
public class ExamResultMapper {

    public ExamResult toEntity(ExamResultRequest request) {

        return ExamResult.builder()
                .examId(request.examId())
                .studentEnrollmentId(request.studentEnrollmentId())
                .subjectId(request.subjectId())
                .maxMarks(request.maxMarks())
                .build();
    }

    public ExamResultResponse toResponse(ExamResult entity) {

        double percentage =
                (entity.getMarksObtained() / entity.getMaxMarks()) * 100;

        return ExamResultResponse.builder()
                .id(entity.getId())
                .examId(entity.getExamId())
                .studentEnrollmentId(entity.getStudentEnrollmentId())
                .subjectId(entity.getSubjectId())
                .marksObtained(entity.getMarksObtained())
                .maxMarks(entity.getMaxMarks())
                .percentage(percentage)
                .grade(entity.getGrade())
                .resultStatus(entity.getResultStatus())
                .absent(entity.getAbsent())
                .build();
    }
}
