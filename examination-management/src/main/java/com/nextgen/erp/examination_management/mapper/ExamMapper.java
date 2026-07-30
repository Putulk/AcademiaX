package com.nextgen.erp.examination_management.mapper;

import com.nextgen.erp.examination_management.dto.ExamRequest;
import com.nextgen.erp.examination_management.dto.ExamResponse;
import com.nextgen.erp.examination_management.entity.Exam;
import com.nextgen.erp.examination_management.enums.ExamStatus;
import org.springframework.stereotype.Component;

@Component
public class ExamMapper {

    public Exam toEntity(ExamRequest request) {

        return Exam.builder()
                .name(request.name())
                .academicYear(request.academicYear())
                .classId(request.classId())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .status(request.status() != null
                        ? request.status()
                        : ExamStatus.DRAFT)
                .description(request.description())
                .build();
    }

    public ExamResponse toResponse(Exam entity) {

        return ExamResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .academicYear(entity.getAcademicYear())
                .classId(entity.getClassId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .description(entity.getDescription())
                .build();
    }
}