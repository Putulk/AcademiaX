package com.nextgen.erp.auth.entity;

import com.nextgen.erp.auth.enums.PermissionName;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "permissions")
public class Permission extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private PermissionName name;

    @Column(length = 255)
    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<Roles> roles = new HashSet<>();
}
