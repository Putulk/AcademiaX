package com.nextgen.erp.examination_management.service;

import com.nextgen.erp.examination_management.dto.ExamScheduleRequest;
import com.nextgen.erp.examination_management.dto.ExamScheduleResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ExamScheduleService {

    ExamScheduleResponse createSchedule(
            ExamScheduleRequest request);

    ExamScheduleResponse updateSchedule(
            UUID id,
            ExamScheduleRequest request);

    ExamScheduleResponse getSchedule(UUID id);

    List<ExamScheduleResponse> getAllSchedules();

    List<ExamScheduleResponse> getSchedulesByExam(
            UUID examId);

    List<ExamScheduleResponse> getSchedulesByClass(
            UUID classId);

    List<ExamScheduleResponse> getSchedulesBySection(
            UUID sectionId);

    List<ExamScheduleResponse> getSchedulesBySubject(
            UUID subjectId);

    List<ExamScheduleResponse> getSchedulesByTeacher(
            UUID teacherId);

    List<ExamScheduleResponse> getSchedulesByDate(
            LocalDate date);

    void deleteSchedule(UUID id);
}