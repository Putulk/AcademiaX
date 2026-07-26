package com.nextgen.erp.student_management.repository;

import com.nextgen.erp.student_management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID>{

    Optional<Student> findByUserProfileId(UUID userProfileId);

    Optional<Student> findByAdmissionNumber(String admissionNumber);

    boolean existsByAdmissionNumber(String admissionNumber);
    boolean existsByUserProfileId(UUID userProfileId);
    boolean existsByRollNumber(String rollNumber);
}
