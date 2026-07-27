package com.nextgen.erp.academic_management.Subject.dto;

import com.nextgen.erp.academic_management.Subject.enums.SubjectType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class SubjectResponse {

    private UUID id;

    private String name;

    private String code;

    private SubjectType type;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
