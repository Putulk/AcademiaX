package com.nextgen.erp.user_management.repository;

import com.nextgen.erp.user_management.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    Page<UserProfile> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String search,String search1,Pageable pageable);
}
