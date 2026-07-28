package com.nextgen.erp.student_management.entity;

import com.nextgen.erp.common.entity.BaseEntity;
import com.nextgen.erp.student_management.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "student_enrollments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "student_id",
                        "academic_year_id"
                })
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentEnrollment extends BaseEntity{

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private UUID academicYearId;

    @Column(nullable = false)
    private UUID classSectionId;

    @Column(nullable = false)
    private String rollNumber;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StudentStatus status = StudentStatus.ACTIVE;

    @Builder.Default
    private Boolean active = true;
}
