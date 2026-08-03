package com.nextgen.erp.platform_core.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityRecordResponse {

    private UUID id;

    private UUID entityDefinitionId;

    private Map<String, Object> data;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
