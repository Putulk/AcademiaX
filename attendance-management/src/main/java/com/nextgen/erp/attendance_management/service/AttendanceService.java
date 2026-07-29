package com.nextgen.erp.attendance_management.service;

import com.nextgen.erp.attendance_management.dto.AttendanceRequest;
import com.nextgen.erp.attendance_management.dto.AttendanceResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AttendanceService{
    AttendanceResponse createAttendance(AttendanceRequest request);

    AttendanceResponse updateAttendance(UUID id,
                                        AttendanceRequest request);

    AttendanceResponse getAttendance(UUID id);

    List<AttendanceResponse> getAllAttendance();

    List<AttendanceResponse> getStudentAttendance(UUID studentEnrollmentId);

    List<AttendanceResponse> getAttendanceByDate(LocalDate date);

    List<AttendanceResponse> getAttendanceByClass(UUID classId);

    void deleteAttendance(UUID id);
}
