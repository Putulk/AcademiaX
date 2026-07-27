package com.nextgen.erp.student_management.mapper;

import com.nextgen.erp.student_management.dto.StudentRequest;
import com.nextgen.erp.student_management.dto.StudentResponse;
import com.nextgen.erp.student_management.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public Student toEntity(StudentRequest request) {

        return Student.builder()
                .userProfileId(request.getUserProfileId())
                .classId(request.getClassId())
                .sectionId(request.getSectionId())
                .admissionNumber(request.getAdmissionNumber())
                .rollNumber(request.getRollNumber())
                .academicYear(request.getAcademicYear())
                .admissionDate(request.getAdmissionDate())
                .bloodGroup(request.getBloodGroup())
                .religion(request.getReligion())
                .category(request.getCategory())
                .house(request.getHouse())
                .build();
    }

    public StudentResponse toResponse(Student student) {

        return StudentResponse.builder()
                .id(student.getId())
                .userProfileId(student.getUserProfileId())
                .classId(student.getClassId())
                .sectionId(student.getSectionId())
                .admissionNumber(student.getAdmissionNumber())
                .rollNumber(student.getRollNumber())
                .academicYear(student.getAcademicYear())
                .admissionDate(student.getAdmissionDate())
                .bloodGroup(student.getBloodGroup())
                .religion(student.getReligion())
                .category(student.getCategory())
                .house(student.getHouse())
                .status(student.getStatus())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}