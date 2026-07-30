package com.nextgen.erp.examination_management.controller;

import com.nextgen.erp.common.dto.ApiResponse;
import com.nextgen.erp.examination_management.dto.ExamRequest;
import com.nextgen.erp.examination_management.dto.ExamResponse;
import com.nextgen.erp.examination_management.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping
    public ResponseEntity<ApiResponse<ExamResponse>> createExam(
            @Valid @RequestBody ExamRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examService.createExam(request)
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExamResponse>> updateExam(
            @PathVariable UUID id,
            @Valid @RequestBody ExamRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examService.updateExam(id, request)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExamResponse>> getExam(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examService.getExam(id)
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getAllExams() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examService.getAllExams()
                )
        );
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getExamsByClass(
            @PathVariable UUID classId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examService.getExamsByClass(classId)
                )
        );
    }

    @GetMapping("/academic-year/{academicYear}")
    public ResponseEntity<ApiResponse<List<ExamResponse>>>
    getExamsByAcademicYear(
            @PathVariable String academicYear) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        examService.getExamsByAcademicYear(
                                academicYear
                        )
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteExam(
            @PathVariable UUID id) {

        examService.deleteExam(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Exam deleted successfully"
                )
        );
    }
}