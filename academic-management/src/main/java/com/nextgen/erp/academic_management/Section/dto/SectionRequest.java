package com.nextgen.erp.academic_management.Section.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SectionRequest {

    @NotBlank(message = "Section name is required")
    private String name;

    @Min(value = 1, message = "Capacity must be greater than zero")
    private Integer capacity;

    private Boolean active;
}