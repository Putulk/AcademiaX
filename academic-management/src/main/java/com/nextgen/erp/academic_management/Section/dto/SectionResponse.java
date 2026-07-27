package com.nextgen.erp.academic_management.Section.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class SectionResponse {

    private UUID id;

    private String name;

    private Integer capacity;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}