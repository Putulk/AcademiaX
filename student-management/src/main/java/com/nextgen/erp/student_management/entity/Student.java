package com.nextgen.erp.student_management.entity;

import com.nextgen.erp.common.entity.BaseEntity;
import com.nextgen.erp.student_management.enums.BloodGroup;
import com.nextgen.erp.student_management.enums.Category;
import com.nextgen.erp.student_management.enums.Religion;
import com.nextgen.erp.student_management.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "students",
        indexes = {
                @Index(name = "idx_student_profile", columnList = "user_profile_id"),
                @Index(name = "idx_student_class", columnList = "class_id"),
                @Index(name = "idx_student_section", columnList = "section_id"),
                @Index(name = "idx_student_status", columnList = "status")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "user_profile_id"),
                @UniqueConstraint(columnNames = "admission_number"),
                @UniqueConstraint(columnNames = {"class_id", "roll_number"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student extends BaseEntity {

    @Column(name = "user_profile_id", nullable = false)
    private UUID userProfileId;

    @Column(name = "class_id")
    private UUID classId;

    @Column(name = "section_id")
    private UUID sectionId;;

    @Column(name = "admission_number", nullable = false, length = 30)
    private String admissionNumber;

    @Column(name = "roll_number", nullable = false, length = 20)
    private String rollNumber;

    @Column(name = "academic_year", nullable = false, length = 20)
    private String academicYear;

    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group")
    private BloodGroup bloodGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "religion")
    private Religion religion;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(length = 30)
    private String house;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StudentStatus status = StudentStatus.ACTIVE;
}