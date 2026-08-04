package com.nextgen.erp.examination_management.controller;

import com.nextgen.erp.common.dto.ApiResponse;
import com.nextgen.erp.examination_management.dto.ExamScheduleRequest;
import com.nextgen.erp.examination_management.dto.ExamScheduleResponse;
import com.nextgen.erp.examination_management.service.ExamScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exam-schedules")
@RequiredArgsConstructor
public class ExamScheduleController {

    private final ExamScheduleService examScheduleService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT','TEACHER')")
    @PostMapping
    public ResponseEntity<ApiResponse<ExamScheduleResponse>> createSchedule(
            @Valid @RequestBody ExamScheduleRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examScheduleService.createSchedule(request)
                )
        );
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT','TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExamScheduleResponse>> updateSchedule(
            @PathVariable UUID id,
            @Valid @RequestBody ExamScheduleRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examScheduleService.updateSchedule(id, request)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExamScheduleResponse>> getSchedule(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examScheduleService.getSchedule(id)
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExamScheduleResponse>>> getAllSchedules() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examScheduleService.getAllSchedules()
                )
        );
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<ApiResponse<List<ExamScheduleResponse>>> getSchedulesByExam(
            @PathVariable UUID examId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examScheduleService.getSchedulesByExam(examId)
                )
        );
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<ApiResponse<List<ExamScheduleResponse>>> getSchedulesByClass(
            @PathVariable UUID classId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examScheduleService.getSchedulesByClass(classId)
                )
        );
    }

    @GetMapping("/section/{sectionId}")
    public ResponseEntity<ApiResponse<List<ExamScheduleResponse>>> getSchedulesBySection(
            @PathVariable UUID sectionId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examScheduleService.getSchedulesBySection(sectionId)
                )
        );
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<ApiResponse<List<ExamScheduleResponse>>> getSchedulesBySubject(
            @PathVariable UUID subjectId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examScheduleService.getSchedulesBySubject(subjectId)
                )
        );
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ApiResponse<List<ExamScheduleResponse>>> getSchedulesByTeacher(
            @PathVariable UUID teacherId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examScheduleService.getSchedulesByTeacher(teacherId)
                )
        );
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<ApiResponse<List<ExamScheduleResponse>>> getSchedulesByDate(
            @PathVariable LocalDate date) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examScheduleService.getSchedulesByDate(date)
                )
        );
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGEMENT','TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSchedule(
            @PathVariable UUID id) {

        examScheduleService.deleteSchedule(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Exam schedule deleted successfully"
                )
        );
    }
}