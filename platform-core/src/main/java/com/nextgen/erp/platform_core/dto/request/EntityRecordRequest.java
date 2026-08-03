package com.nextgen.erp.platform_core.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record EntityRecordRequest(

        @NotNull(message = "Data is required")
        Map<String, Object> data
) {
}
