package com.nextgen.erp.examination_management.entity;

import com.nextgen.erp.common.entity.BaseEntity;
import com.nextgen.erp.examination_management.enums.ExamStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "exams",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"name", "academic_year"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "academic_year",
            nullable = false,
            length = 20)
    private String academicYear;

    @Column(name = "class_id",
            nullable = false)
    private UUID classId;

    @Column(name = "start_date",
            nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date",
            nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ExamStatus status = ExamStatus.DRAFT;

    @Column(length = 500)
    private String description;
}
