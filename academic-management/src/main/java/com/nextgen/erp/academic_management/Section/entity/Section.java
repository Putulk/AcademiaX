package com.nextgen.erp.academic_management.Section.entity;

import com.nextgen.erp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "sections",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section extends BaseEntity {

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    @Builder.Default
    private Integer capacity = 40;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}