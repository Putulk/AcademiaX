package com.nextgen.erp.attendance_management.controller;

import com.nextgen.erp.attendance_management.dto.AttendanceRequest;
import com.nextgen.erp.attendance_management.dto.AttendanceResponse;
import com.nextgen.erp.attendance_management.service.AttendanceService;
import com.nextgen.erp.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT','TEACHER')")
    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceResponse>> createAttendance(
            @Valid @RequestBody AttendanceRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(attendanceService.createAttendance(request))
        );
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT','TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AttendanceResponse>> updateAttendance(
            @PathVariable UUID id,
            @Valid @RequestBody AttendanceRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(attendanceService.updateAttendance(id, request))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AttendanceResponse>> getAttendance(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(attendanceService.getAttendance(id))
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAllAttendance() {

        return ResponseEntity.ok(
                ApiResponse.success(attendanceService.getAllAttendance())
        );
    }

    @GetMapping("/student/{studentEnrollmentId}")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getStudentAttendance(
            @PathVariable UUID studentEnrollmentId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        attendanceService.getStudentAttendance(studentEnrollmentId))
        );
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAttendanceByDate(
            @PathVariable LocalDate date) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        attendanceService.getAttendanceByDate(date))
        );
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAttendanceByClass(
            @PathVariable UUID classId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        attendanceService.getAttendanceByClass(classId))
        );
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT','TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAttendance(
            @PathVariable UUID id) {

        attendanceService.deleteAttendance(id);

        return ResponseEntity.ok(
                ApiResponse.success("Attendance deleted successfully"));
    }
}