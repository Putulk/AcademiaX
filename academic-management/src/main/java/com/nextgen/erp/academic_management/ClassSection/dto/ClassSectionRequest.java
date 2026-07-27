package com.nextgen.erp.academic_management.ClassSection.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ClassSectionRequest {

    @NotNull
    private UUID academicYearId;

    @NotNull
    private UUID classRoomId;

    @NotNull
    private UUID sectionId;

    private UUID classTeacherId;

    private String roomNumber;

    private Integer capacity;

    private Boolean active;
}
