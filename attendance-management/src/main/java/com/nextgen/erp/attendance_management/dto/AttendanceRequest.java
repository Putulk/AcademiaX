package com.nextgen.erp.attendance_management.dto;

import com.nextgen.erp.attendance_management.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRequest {

    @NotNull
    private UUID studentEnrollmentId;

    @NotNull
    private UUID classId;

    @NotNull
    private UUID sectionId;

    @NotNull
    private UUID subjectId;

    @NotNull
    private UUID teacherId;

    @NotNull
    private LocalDate attendanceDate;

    @NotNull
    private AttendanceStatus status;

    private String remarks;
}