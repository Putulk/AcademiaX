package com.nextgen.erp.examination_management.entity;

import com.nextgen.erp.common.entity.BaseEntity;
import com.nextgen.erp.examination_management.enums.Grade;
import com.nextgen.erp.examination_management.enums.ResultStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "exam_results",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "exam_id",
                                "student_enrollment_id",
                                "subject_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResult extends BaseEntity {

    @Column(name = "exam_id", nullable = false)
    private UUID examId;

    @Column(name = "student_enrollment_id", nullable = false)
    private UUID studentEnrollmentId;

    @Column(name = "subject_id", nullable = false)
    private UUID subjectId;

    @Column(name = "marks_obtained", nullable = false)
    private Double marksObtained;

    @Column(name = "max_marks", nullable = false)
    private Integer maxMarks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_status", nullable = false, length = 20)
    private ResultStatus resultStatus;

    @Column(nullable = false)
    @Builder.Default
    private Boolean absent = false;
}
