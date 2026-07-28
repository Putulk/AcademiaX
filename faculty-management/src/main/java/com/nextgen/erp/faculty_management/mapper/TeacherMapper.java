package com.nextgen.erp.faculty_management.mapper;

import com.nextgen.erp.faculty_management.dto.TeacherRequest;
import com.nextgen.erp.faculty_management.dto.TeacherResponse;
import com.nextgen.erp.faculty_management.entity.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {

    public Teacher toEntity(TeacherRequest request) {

        return Teacher.builder()
                .userProfileId(request.getUserProfileId())
                .employeeCode(request.getEmployeeCode())
                .designation(request.getDesignation())
                .department(request.getDepartment())
                .joiningDate(request.getJoiningDate())
                .experienceYears(request.getExperienceYears())
                .salary(request.getSalary())
                .active(request.getActive() == null ? true : request.getActive())
                .build();
    }

    public TeacherResponse toResponse(Teacher entity) {

        return TeacherResponse.builder()
                .id(entity.getId())
                .userProfileId(entity.getUserProfileId())
                .employeeCode(entity.getEmployeeCode())
                .designation(entity.getDesignation())
                .department(entity.getDepartment())
                .joiningDate(entity.getJoiningDate())
                .experienceYears(entity.getExperienceYears())
                .salary(entity.getSalary())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(Teacher teacher, TeacherRequest request) {

        teacher.setUserProfileId(request.getUserProfileId());
        teacher.setEmployeeCode(request.getEmployeeCode());
        teacher.setDesignation(request.getDesignation());
        teacher.setDepartment(request.getDepartment());
        teacher.setJoiningDate(request.getJoiningDate());
        teacher.setExperienceYears(request.getExperienceYears());
        teacher.setSalary(request.getSalary());

        if (request.getActive() != null) {
            teacher.setActive(request.getActive());
        }
    }
}