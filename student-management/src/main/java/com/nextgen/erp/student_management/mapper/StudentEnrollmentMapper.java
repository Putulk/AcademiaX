package com.nextgen.erp.student_management.mapper;

import com.nextgen.erp.student_management.dto.StudentEnrollmentRequest;
import com.nextgen.erp.student_management.dto.StudentEnrollmentResponse;
import com.nextgen.erp.student_management.entity.StudentEnrollment;
import com.nextgen.erp.student_management.enums.StudentStatus;
import org.springframework.stereotype.Component;

@Component
public class StudentEnrollmentMapper {

    public StudentEnrollment toEntity(StudentEnrollmentRequest request) {

        return StudentEnrollment.builder()
                .studentId(request.getStudentId())
                .academicYearId(request.getAcademicYearId())
                .classSectionId(request.getClassSectionId())
                .rollNumber(request.getRollNumber())
                .status(request.getStatus() == null
                        ? StudentStatus.ACTIVE
                        : request.getStatus())
                .active(request.getActive() == null
                        ? true
                        : request.getActive())
                .build();
    }

    public StudentEnrollmentResponse toResponse(StudentEnrollment entity) {

        return StudentEnrollmentResponse.builder()
                .id(entity.getId())
                .studentId(entity.getStudentId())
                .academicYearId(entity.getAcademicYearId())
                .classSectionId(entity.getClassSectionId())
                .rollNumber(entity.getRollNumber())
                .status(entity.getStatus())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(StudentEnrollment entity,
                             StudentEnrollmentRequest request) {

        entity.setStudentId(request.getStudentId());
        entity.setAcademicYearId(request.getAcademicYearId());
        entity.setClassSectionId(request.getClassSectionId());
        entity.setRollNumber(request.getRollNumber());

        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }

        if (request.getActive() != null) {
            entity.setActive(request.getActive());
        }
    }
}