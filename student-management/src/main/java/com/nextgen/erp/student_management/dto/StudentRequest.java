package com.nextgen.erp.student_management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Schema(description = "Request object for creating or updating a student")
public class StudentRequest {

    @NotNull
    @Schema(
            description = "User Profile ID",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID userProfileId;

    @NotBlank
    @Schema(example = "ADM2026001")
    private String admissionNumber;

    @NotBlank
    @Schema(example = "101")
    private String rollNumber;

    @Schema(example = "2026-2027")
    private String academicYear;

    @Schema(example = "2026-04-01")
    private LocalDate admissionDate;

    @Schema(example = "O+")
    private String bloodGroup;

    @Schema(example = "Hindu")
    private String religion;

    @Schema(example = "General")
    private String category;

    @Schema(example = "Red House")
    private String house;

    @Schema(example = "true")
    private Boolean active;
}