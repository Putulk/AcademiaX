package com.nextgen.erp.student_management.repository;

import com.nextgen.erp.student_management.entity.Student;
import com.nextgen.erp.student_management.enums.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    Optional<Student> findByAdmissionNumber(String admissionNumber);

    Optional<Student> findByUserProfileId(UUID userProfileId);

    List<Student> findByClassId(UUID classId);

    List<Student> findBySectionId(UUID sectionId);

    List<Student> findByStatus(StudentStatus status);

    boolean existsByAdmissionNumber(String admissionNumber);

    boolean existsByUserProfileId(UUID userProfileId);

    boolean existsByClassIdAndRollNumber(UUID classId,
                                         String rollNumber);
}
