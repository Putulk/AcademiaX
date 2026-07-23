package com.nextgen.erp.auth.repository;

import com.nextgen.erp.auth.entity.Roles;
import com.nextgen.erp.common.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Roles, UUID>{

    Optional<Roles> findByName(RoleName name);

    boolean existsByName(String name);
}
