package com.nextgen.erp.examination_management.service.impl;

import com.nextgen.erp.common.dto.ApiResponse;
import com.nextgen.erp.examination_management.client.AcademicClient;
import com.nextgen.erp.examination_management.client.FacultyClient;
import com.nextgen.erp.examination_management.dto.ExamScheduleRequest;
import com.nextgen.erp.examination_management.dto.ExamScheduleResponse;
import com.nextgen.erp.examination_management.entity.Exam;
import com.nextgen.erp.examination_management.entity.ExamSchedule;
import com.nextgen.erp.examination_management.mapper.ExamScheduleMapper;
import com.nextgen.erp.examination_management.repository.ExamRepository;
import com.nextgen.erp.examination_management.repository.ExamScheduleRepository;
import com.nextgen.erp.examination_management.service.ExamScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamScheduleServiceImpl
        implements ExamScheduleService {

    private final ExamScheduleRepository scheduleRepository;
    private final ExamScheduleMapper scheduleMapper;
    private final ExamRepository examRepository;
    private final AcademicClient academicClient;
    private final FacultyClient facultyClient;

    @Override
    public ExamScheduleResponse createSchedule(
            ExamScheduleRequest request) {

        validateRequest(request);

        if (scheduleRepository
                .existsByExamIdAndSubjectIdAndSectionId(
                        request.examId(),
                        request.subjectId(),
                        request.sectionId())) {

            throw new RuntimeException(
                    "Schedule already exists for this subject and section."
            );
        }

        checkTimeConflict(request);

        ExamSchedule schedule =
                scheduleMapper.toEntity(request);

        schedule = scheduleRepository.save(schedule);

        return scheduleMapper.toResponse(schedule);
    }

    private void validateRequest(
            ExamScheduleRequest request) {

        // 1. Validate Exam
        Exam exam = examRepository.findById(request.examId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Exam not found: "
                                        + request.examId()));

        // 2. Validate dates
        if (request.endTime()
                .isBefore(request.startTime())
                || request.endTime()
                .equals(request.startTime())) {

            throw new RuntimeException(
                    "End time must be after start time."
            );
        }

        if (request.examDate()
                .isBefore(exam.getStartDate())
                || request.examDate()
                .isAfter(exam.getEndDate())) {

            throw new RuntimeException(
                    "Exam date must be within the exam period."
            );
        }

        // 3. Validate Class
        validateClass(request.classId());

        // 4. Validate Subject
        validateSubject(request.subjectId());

        // 5. Validate Section
        validateSection(request.sectionId());

        // 6. Validate Teacher
        validateTeacher(request.teacherId());
    }

    private void validateClass(UUID classId) {

        ApiResponse<Boolean> response =
                academicClient.classExists(classId);

        if (!Boolean.TRUE.equals(response.getData())) {

            throw new RuntimeException(
                    "Class not found: " + classId);
        }
    }

    private void validateSubject(UUID subjectId) {

        ApiResponse<Boolean> response =
                academicClient.subjectExists(subjectId);

        if (!Boolean.TRUE.equals(response.getData())) {

            throw new RuntimeException(
                    "Subject not found: " + subjectId);
        }
    }

    private void validateSection(UUID sectionId) {

        ApiResponse<Boolean> response =
                academicClient.sectionExists(sectionId);

        if (!Boolean.TRUE.equals(response.getData())) {

            throw new RuntimeException(
                    "Section not found: " + sectionId);
        }
    }

    private void validateTeacher(UUID teacherId) {

        ApiResponse<Boolean> response =
                facultyClient.teacherExists(teacherId);

        if (!Boolean.TRUE.equals(response.getData())) {

            throw new RuntimeException(
                    "Teacher not found: " + teacherId);
        }
    }

    private void checkTimeConflict(
            ExamScheduleRequest request) {

        if (!scheduleRepository
                .findByExamDateAndTeacherIdAndStartTimeLessThanAndEndTimeGreaterThan(
                        request.examDate(),
                        request.teacherId(),
                        request.endTime(),
                        request.startTime())
                .isEmpty()) {

            throw new RuntimeException(
                    "Teacher already has an exam scheduled during this time."
            );
        }

        if (!scheduleRepository
                .findByExamDateAndSectionIdAndStartTimeLessThanAndEndTimeGreaterThan(
                        request.examDate(),
                        request.sectionId(),
                        request.endTime(),
                        request.startTime())
                .isEmpty()) {

            throw new RuntimeException(
                    "Section already has an exam scheduled during this time."
            );
        }

        if (request.roomNumber() != null &&
                !request.roomNumber().isBlank() &&
                !scheduleRepository
                        .findByExamDateAndRoomNumberAndStartTimeLessThanAndEndTimeGreaterThan(
                                request.examDate(),
                                request.roomNumber(),
                                request.endTime(),
                                request.startTime())
                        .isEmpty()) {

            throw new RuntimeException(
                    "Room is already occupied during this time."
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ExamScheduleResponse getSchedule(UUID id) {

        ExamSchedule schedule =
                scheduleRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Exam schedule not found."));

        return scheduleMapper.toResponse(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamScheduleResponse> getAllSchedules() {

        return scheduleRepository.findAll()
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamScheduleResponse> getSchedulesByExam(
            UUID examId) {

        return scheduleRepository.findByExamId(examId)
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamScheduleResponse> getSchedulesByClass(
            UUID classId) {

        return scheduleRepository.findByClassId(classId)
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamScheduleResponse> getSchedulesBySection(
            UUID sectionId) {

        return scheduleRepository.findBySectionId(sectionId)
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamScheduleResponse> getSchedulesBySubject(
            UUID subjectId) {

        return scheduleRepository.findBySubjectId(subjectId)
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamScheduleResponse> getSchedulesByTeacher(
            UUID teacherId) {

        return scheduleRepository.findByTeacherId(teacherId)
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamScheduleResponse> getSchedulesByDate(
            LocalDate date) {

        return scheduleRepository.findByExamDate(date)
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ExamScheduleResponse updateSchedule(
            UUID id,
            ExamScheduleRequest request) {

        ExamSchedule existing = scheduleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Exam schedule not found"));

        validateRequest(request);

        checkTimeConflict(id, request);

        existing.setExamDate(request.examDate());
        existing.setStartTime(request.startTime());
        existing.setEndTime(request.endTime());
        existing.setRoomNumber(request.roomNumber());
        existing.setExamId(request.examId());
        existing.setSubjectId(request.subjectId());
        existing.setTeacherId(request.teacherId());
        existing.setClassId(request.classId());
        existing.setSectionId(request.sectionId());

        return scheduleMapper.toResponse(
                scheduleRepository.save(existing)
        );
    }
    @Override
    public void deleteSchedule(UUID id){

        if(!scheduleRepository.existsById(id)){

            throw new RuntimeException(
                    "Exam schedule not found.");
        }

        scheduleRepository.deleteById(id);
    }

    private void checkTimeConflict(
            UUID scheduleId,
            ExamScheduleRequest request) {

        if (!scheduleRepository.findTeacherConflicts(
                scheduleId,
                request.examDate(),
                request.teacherId(),
                request.startTime(),
                request.endTime()
        ).isEmpty()) {

            throw new RuntimeException(
                    "Teacher already has an exam during this time."
            );
        }

        if (!scheduleRepository.findSectionConflicts(
                scheduleId,
                request.examDate(),
                request.sectionId(),
                request.startTime(),
                request.endTime()
        ).isEmpty()) {

            throw new RuntimeException(
                    "Section already has an exam during this time."
            );
        }

        if (request.roomNumber() != null &&
                !request.roomNumber().isBlank() &&
                !scheduleRepository.findRoomConflicts(
                        scheduleId,
                        request.examDate(),
                        request.roomNumber(),
                        request.startTime(),
                        request.endTime()
                ).isEmpty()) {

            throw new RuntimeException(
                    "Room is already occupied during this time."
            );
        }
    }
}