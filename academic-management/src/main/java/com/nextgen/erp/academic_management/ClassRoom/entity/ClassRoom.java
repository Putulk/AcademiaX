package com.nextgen.erp.academic_management.ClassRoom.entity;

import com.nextgen.erp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Data;

@Builder
@Entity
@Table(
        name = "class_rooms",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        }
)
@Data
public class ClassRoom extends BaseEntity{

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer displayOrder;

    @Builder.Default
    private Boolean active = true;
}
