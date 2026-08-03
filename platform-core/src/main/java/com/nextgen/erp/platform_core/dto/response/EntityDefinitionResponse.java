package com.nextgen.erp.platform_core.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityDefinitionResponse {

    private UUID id;

    private UUID tenantId;

    private String name;

    private String pluralLabel;

    private String description;

    private String displayFieldName;

    private Boolean active;
}
