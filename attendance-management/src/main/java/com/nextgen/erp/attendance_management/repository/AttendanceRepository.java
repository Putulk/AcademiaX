package com.nextgen.erp.attendance_management.repository;

import com.nextgen.erp.attendance_management.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    List<Attendance> findByStudentEnrollmentId(UUID studentEnrollmentId);

    List<Attendance> findByAttendanceDate(LocalDate attendanceDate);

    List<Attendance> findByClassId(UUID classId);

    List<Attendance> findBySectionId(UUID sectionId);

    List<Attendance> findByTeacherId(UUID teacherId);

    boolean existsByStudentEnrollmentIdAndAttendanceDateAndSubjectId(
            UUID studentEnrollmentId,
            LocalDate attendanceDate,
            UUID subjectId
    );
}