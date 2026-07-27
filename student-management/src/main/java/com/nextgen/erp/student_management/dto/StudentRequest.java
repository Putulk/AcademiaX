package com.nextgen.erp.student_management.dto;

import com.nextgen.erp.student_management.enums.BloodGroup;
import com.nextgen.erp.student_management.enums.Category;
import com.nextgen.erp.student_management.enums.Religion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class StudentRequest {

    @NotNull
    private UUID userProfileId;

    private UUID classId;

    private UUID sectionId;

    @NotBlank
    @Schema(example = "ADM20260001")
    private String admissionNumber;

    @NotBlank
    @Schema(example = "12")
    private String rollNumber;

    @NotBlank
    @Schema(example = "2026-2027")
    private String academicYear;

    @NotNull
    private LocalDate admissionDate;

    private BloodGroup bloodGroup;

    private Religion religion;

    private Category category;

    @Schema(example = "Red")
    private String house;
}