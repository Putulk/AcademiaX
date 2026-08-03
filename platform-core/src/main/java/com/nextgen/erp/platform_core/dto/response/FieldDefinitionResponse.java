package com.nextgen.erp.platform_core.dto.response;

import com.nextgen.erp.platform_core.enums.DataType;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldDefinitionResponse {

    private UUID id;

    private UUID entityDefinitionId;

    private String name;

    private String label;

    private DataType dataType;

    private Boolean required;

    private UUID referenceTargetEntityDefinitionId;

    private List<String> enumOptions;

    private Integer displayOrder;
}
