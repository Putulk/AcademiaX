package com.nextgen.erp.auth.repository;

import com.nextgen.erp.auth.entity.Permission;
import com.nextgen.erp.auth.enums.PermissionName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID>{

    Optional<Permission> findByName(PermissionName name);
}
