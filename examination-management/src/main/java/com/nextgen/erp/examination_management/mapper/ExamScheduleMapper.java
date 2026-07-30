package com.nextgen.erp.examination_management.mapper;

import com.nextgen.erp.examination_management.dto.ExamScheduleRequest;
import com.nextgen.erp.examination_management.dto.ExamScheduleResponse;
import com.nextgen.erp.examination_management.entity.ExamSchedule;
import org.springframework.stereotype.Component;

@Component
public class ExamScheduleMapper {

    public ExamSchedule toEntity(
            ExamScheduleRequest request) {

        return ExamSchedule.builder()
                .examId(request.examId())
                .subjectId(request.subjectId())
                .teacherId(request.teacherId())
                .classId(request.classId())
                .sectionId(request.sectionId())
                .examDate(request.examDate())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .roomNumber(request.roomNumber())
                .maxMarks(request.maxMarks())
                .build();
    }

    public ExamScheduleResponse toResponse(
            ExamSchedule entity) {

        return ExamScheduleResponse.builder()
                .id(entity.getId())
                .examId(entity.getExamId())
                .subjectId(entity.getSubjectId())
                .teacherId(entity.getTeacherId())
                .classId(entity.getClassId())
                .sectionId(entity.getSectionId())
                .examDate(entity.getExamDate())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .roomNumber(entity.getRoomNumber())
                .maxMarks(entity.getMaxMarks())
                .build();
    }
}