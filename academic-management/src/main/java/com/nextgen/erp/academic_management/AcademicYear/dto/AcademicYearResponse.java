package com.nextgen.erp.academic_management.AcademicYear.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AcademicYearResponse {

    private UUID id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}