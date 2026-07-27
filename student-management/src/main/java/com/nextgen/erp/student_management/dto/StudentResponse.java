package com.nextgen.erp.student_management.dto;

import com.nextgen.erp.student_management.enums.BloodGroup;
import com.nextgen.erp.student_management.enums.Category;
import com.nextgen.erp.student_management.enums.Religion;
import com.nextgen.erp.student_management.enums.StudentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class StudentResponse {

    private UUID id;

    private UUID userProfileId;

    private UUID classId;

    private UUID sectionId;

    private String admissionNumber;

    private String rollNumber;

    private String academicYear;

    private LocalDate admissionDate;

    private BloodGroup bloodGroup;

    private Religion religion;

    private Category category;

    private String house;

    private StudentStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}