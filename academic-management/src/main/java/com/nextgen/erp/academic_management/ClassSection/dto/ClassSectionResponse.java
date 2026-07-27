package com.nextgen.erp.academic_management.ClassSection.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class ClassSectionResponse {

    private UUID id;

    private UUID academicYearId;

    private UUID classRoomId;

    private UUID sectionId;

    private UUID classTeacherId;

    private String roomNumber;

    private Integer capacity;

    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
