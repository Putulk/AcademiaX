package com.nextgen.erp.faculty_management.mapper;

import com.nextgen.erp.faculty_management.dto.TeacherAssignmentRequest;
import com.nextgen.erp.faculty_management.dto.TeacherAssignmentResponse;
import com.nextgen.erp.faculty_management.entity.TeacherAssignment;
import org.springframework.stereotype.Component;

@Component
public class TeacherAssignmentMapper {

    public TeacherAssignment toEntity(TeacherAssignmentRequest request) {

        return TeacherAssignment.builder()
                .teacherId(request.getTeacherId())
                .classSectionId(request.getClassSectionId())
                .subjectId(request.getSubjectId())
                .academicYearId(request.getAcademicYearId())
                .active(request.getActive() == null ? true : request.getActive())
                .build();
    }

    public TeacherAssignmentResponse toResponse(TeacherAssignment entity) {

        return TeacherAssignmentResponse.builder()
                .id(entity.getId())
                .teacherId(entity.getTeacherId())
                .classSectionId(entity.getClassSectionId())
                .subjectId(entity.getSubjectId())
                .academicYearId(entity.getAcademicYearId())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(TeacherAssignment entity,
                             TeacherAssignmentRequest request) {

        entity.setTeacherId(request.getTeacherId());
        entity.setClassSectionId(request.getClassSectionId());
        entity.setSubjectId(request.getSubjectId());
        entity.setAcademicYearId(request.getAcademicYearId());

        if (request.getActive() != null) {
            entity.setActive(request.getActive());
        }
    }
}