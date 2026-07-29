package com.nextgen.erp.attendance_management.entity;

import com.nextgen.erp.attendance_management.enums.AttendanceStatus;
import com.nextgen.erp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "attendance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance extends BaseEntity {

    @Column(nullable = false)
    private UUID studentEnrollmentId;

    @Column(nullable = false)
    private UUID classId;

    @Column(nullable = false)
    private UUID sectionId;

    @Column(nullable = false)
    private UUID subjectId;

    @Column(nullable = false)
    private UUID teacherId;

    @Column(nullable = false)
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    private String remarks;
}