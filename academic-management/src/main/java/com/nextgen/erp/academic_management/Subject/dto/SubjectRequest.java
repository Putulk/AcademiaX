package com.nextgen.erp.academic_management.Subject.dto;

import com.nextgen.erp.academic_management.Subject.enums.SubjectType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubjectRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @NotNull
    private SubjectType type;

    private Boolean active;
}
