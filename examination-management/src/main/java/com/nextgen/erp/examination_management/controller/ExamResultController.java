package com.nextgen.erp.examination_management.controller;

import com.nextgen.erp.common.dto.ApiResponse;
import com.nextgen.erp.examination_management.dto.ExamResultRequest;
import com.nextgen.erp.examination_management.dto.ExamResultResponse;
import com.nextgen.erp.examination_management.service.ExamResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exam-results")
@RequiredArgsConstructor
public class ExamResultController {

    private final ExamResultService examResultService;

    @PostMapping
    public ResponseEntity<ApiResponse<ExamResultResponse>> createResult(
            @Valid @RequestBody ExamResultRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examResultService.createResult(request)
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExamResultResponse>> updateResult(
            @PathVariable UUID id,
            @Valid @RequestBody ExamResultRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examResultService.updateResult(id, request)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExamResultResponse>> getResult(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examResultService.getResult(id)
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExamResultResponse>>> getAllResults() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examResultService.getAllResults()
                )
        );
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<ApiResponse<List<ExamResultResponse>>> getResultsByExam(
            @PathVariable UUID examId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examResultService.getResultsByExam(examId)
                )
        );
    }

    @GetMapping("/student-enrollment/{studentEnrollmentId}")
    public ResponseEntity<ApiResponse<List<ExamResultResponse>>> getResultsByStudentEnrollment(
            @PathVariable UUID studentEnrollmentId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examResultService.getResultsByStudentEnrollment(
                                studentEnrollmentId)
                )
        );
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<ApiResponse<List<ExamResultResponse>>> getResultsBySubject(
            @PathVariable UUID subjectId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examResultService.getResultsBySubject(subjectId)
                )
        );
    }

    @GetMapping("/exam/{examId}/student-enrollment/{studentEnrollmentId}")
    public ResponseEntity<ApiResponse<List<ExamResultResponse>>> getResultCard(
            @PathVariable UUID examId,
            @PathVariable UUID studentEnrollmentId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examResultService.getResultCard(
                                examId, studentEnrollmentId)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteResult(
            @PathVariable UUID id) {

        examResultService.deleteResult(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Exam result deleted successfully"
                )
        );
    }
}
