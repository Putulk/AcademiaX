package com.nextgen.erp.faculty_management.entity;

import com.nextgen.erp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "teacher_assignments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "teacher_id",
                        "class_section_id",
                        "subject_id",
                        "academic_year_id"
                })
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherAssignment extends BaseEntity {

    @Column(name = "teacher_id", nullable = false)
    private UUID teacherId;

    @Column(name = "class_section_id", nullable = false)
    private UUID classSectionId;

    @Column(name = "subject_id", nullable = false)
    private UUID subjectId;

    @Column(name = "academic_year_id", nullable = false)
    private UUID academicYearId;

    @Builder.Default
    private Boolean active = true;
}