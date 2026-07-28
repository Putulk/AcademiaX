package com.nextgen.erp.faculty_management.repository;

import com.nextgen.erp.faculty_management.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByUserProfileId(UUID userProfileId);
}
