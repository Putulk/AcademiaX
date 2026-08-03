package com.nextgen.erp.platform_core.entity;

import com.nextgen.erp.common.entity.BaseEntity;
import com.nextgen.erp.platform_core.enums.DataType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "field_definitions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"entity_definition_id", "name"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldDefinition extends BaseEntity {

    @Column(name = "entity_definition_id", nullable = false)
    private UUID entityDefinitionId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false, length = 20)
    private DataType dataType;

    @Column(nullable = false)
    @Builder.Default
    private Boolean required = false;

    @Column(name = "reference_target_entity_definition_id")
    private UUID referenceTargetEntityDefinitionId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "enum_options", columnDefinition = "json")
    private List<String> enumOptions;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;
}
