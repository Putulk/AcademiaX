package com.nextgen.erp.platform_core.entity;

import com.nextgen.erp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "entity_definitions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"tenant_id", "name"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityDefinition extends BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "plural_label", nullable = false, length = 100)
    private String pluralLabel;

    @Column(length = 500)
    private String description;

    @Column(name = "display_field_name", nullable = false, length = 100)
    private String displayFieldName;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
