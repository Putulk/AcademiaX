package com.nextgen.erp.attendance_management.service.impl;

import com.nextgen.erp.attendance_management.client.AcademicClient;
import com.nextgen.erp.attendance_management.client.FacultyClient;
import com.nextgen.erp.attendance_management.client.StudentClient;
import com.nextgen.erp.attendance_management.dto.AttendanceRequest;
import com.nextgen.erp.attendance_management.dto.AttendanceResponse;
import com.nextgen.erp.attendance_management.entity.Attendance;
import com.nextgen.erp.attendance_management.mapper.AttendanceMapper;
import com.nextgen.erp.attendance_management.repository.AttendanceRepository;
import com.nextgen.erp.attendance_management.service.AttendanceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;
    private final StudentClient studentClient;
    private final FacultyClient facultyClient;
    private final AcademicClient academicClient;

    @Override
    public AttendanceResponse createAttendance(AttendanceRequest request) {

        // Validate Student Enrollment
        if (!Boolean.TRUE.equals(
                studentClient.exists(request.getStudentEnrollmentId()).getData())) {
            throw new RuntimeException("Student Enrollment not found.");
        }

        // Validate Teacher
        if (!Boolean.TRUE.equals(
                facultyClient.exists(request.getTeacherId()).getData())) {
            throw new RuntimeException("Teacher not found.");
        }

        // Validate Class
        if (!Boolean.TRUE.equals(
                academicClient.classExists(request.getClassId()).getData())) {
            throw new RuntimeException("Class not found.");
        }

        // Validate Section
        if (!Boolean.TRUE.equals(
                academicClient.sectionExists(request.getSectionId()).getData())) {
            throw new RuntimeException("Section not found.");
        }

        // Validate Subject
        if (!Boolean.TRUE.equals(
                academicClient.subjectExists(request.getSubjectId()).getData())) {
            throw new RuntimeException("Subject not found.");
        }

        // Prevent duplicate attendance
        if (attendanceRepository.existsByStudentEnrollmentIdAndAttendanceDateAndSubjectId(
                request.getStudentEnrollmentId(),
                request.getAttendanceDate(),
                request.getSubjectId())) {

            throw new RuntimeException(
                    "Attendance already marked for this student.");
        }

        Attendance attendance = attendanceMapper.toEntity(request);

        attendance = attendanceRepository.save(attendance);

        return attendanceMapper.toResponse(attendance);
    }

    @Override
    public AttendanceResponse updateAttendance(UUID id,
                                               AttendanceRequest request) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Attendance not found"));

        attendanceMapper.updateEntity(attendance, request);

        attendanceRepository.save(attendance);

        return attendanceMapper.toResponse(attendance);
    }

    @Override
    public AttendanceResponse getAttendance(UUID id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Attendance not found"));

        return attendanceMapper.toResponse(attendance);
    }

    @Override
    public List<AttendanceResponse> getAllAttendance() {

        return attendanceRepository.findAll()
                .stream()
                .map(attendanceMapper::toResponse)
                .toList();
    }

    @Override
    public List<AttendanceResponse> getStudentAttendance(UUID studentEnrollmentId) {

        return attendanceRepository
                .findByStudentEnrollmentId(studentEnrollmentId)
                .stream()
                .map(attendanceMapper::toResponse)
                .toList();
    }

    @Override
    public List<AttendanceResponse> getAttendanceByDate(LocalDate date) {

        return attendanceRepository
                .findByAttendanceDate(date)
                .stream()
                .map(attendanceMapper::toResponse)
                .toList();
    }

    @Override
    public List<AttendanceResponse> getAttendanceByClass(UUID classId) {

        return attendanceRepository
                .findByClassId(classId)
                .stream()
                .map(attendanceMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteAttendance(UUID id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Attendance not found"));

        attendanceRepository.delete(attendance);
    }
}