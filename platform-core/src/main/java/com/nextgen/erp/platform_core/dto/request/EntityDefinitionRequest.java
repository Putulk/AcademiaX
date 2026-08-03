package com.nextgen.erp.platform_core.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EntityDefinitionRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Plural label is required")
        @Size(max = 100, message = "Plural label must not exceed 100 characters")
        String pluralLabel,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @NotBlank(message = "Display field name is required")
        String displayFieldName,

        boolean active
) {
}
