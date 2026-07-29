package com.nextgen.erp.attendance_management.dto;

import com.nextgen.erp.attendance_management.enums.AttendanceStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResponse {

    private UUID id;

    private UUID studentEnrollmentId;

    private UUID classId;

    private UUID sectionId;

    private UUID subjectId;

    private UUID teacherId;

    private LocalDate attendanceDate;

    private AttendanceStatus status;

    private String remarks;
}