package com.nextgen.erp.platform_core.dto.request;

import com.nextgen.erp.platform_core.enums.DataType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record FieldDefinitionRequest(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Label is required")
        String label,

        @NotNull(message = "Data type is required")
        DataType dataType,

        boolean required,

        UUID referenceTargetEntityDefinitionId,

        List<String> enumOptions,

        Integer displayOrder
) {
}
