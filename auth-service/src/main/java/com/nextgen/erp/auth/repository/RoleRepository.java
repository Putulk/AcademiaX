package com.nextgen.erp.auth.repository;

import com.nextgen.erp.auth.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Roles, UUID>{

    Optional<Roles> findByName(String name);

    boolean existsByName(String name);
}
