package com.nextgen.erp.examination_management.repository;

import com.nextgen.erp.examination_management.entity.ExamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface ExamScheduleRepository
        extends JpaRepository<ExamSchedule, UUID> {
    List<ExamSchedule> findByExamDateAndTeacherIdAndStartTimeLessThanAndEndTimeGreaterThan(
            LocalDate examDate,
            UUID teacherId,
            LocalTime endTime,
            LocalTime startTime
    );

    List<ExamSchedule> findByExamDateAndSectionIdAndStartTimeLessThanAndEndTimeGreaterThan(
            LocalDate examDate,
            UUID sectionId,
            LocalTime endTime,
            LocalTime startTime
    );

    List<ExamSchedule> findByExamDateAndRoomNumberAndStartTimeLessThanAndEndTimeGreaterThan(
            LocalDate examDate,
            String roomNumber,
            LocalTime endTime,
            LocalTime startTime
    );

    boolean existsByExamIdAndSubjectIdAndSectionId(
            UUID examId,
            UUID subjectId,
            UUID sectionId
    );

    List<ExamSchedule> findByExamId(UUID examId);

    List<ExamSchedule> findByClassId(UUID classId);

    List<ExamSchedule> findBySectionId(UUID sectionId);

    List<ExamSchedule> findBySubjectId(UUID subjectId);

    List<ExamSchedule> findByTeacherId(UUID teacherId);

    List<ExamSchedule> findByExamDate(LocalDate examDate);

    @Query("""
    SELECT e FROM ExamSchedule e
    WHERE e.id <> :id
      AND e.examDate = :examDate
      AND e.teacherId = :teacherId
      AND e.startTime < :endTime
      AND e.endTime > :startTime
""")
    List<ExamSchedule> findTeacherConflicts(
            @Param("id") UUID id,
            @Param("examDate") LocalDate examDate,
            @Param("teacherId") UUID teacherId,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("""
    SELECT e FROM ExamSchedule e
    WHERE e.id <> :id
      AND e.examDate = :examDate
      AND e.sectionId = :sectionId
      AND e.startTime < :endTime
      AND e.endTime > :startTime
""")
    List<ExamSchedule> findSectionConflicts(
            @Param("id") UUID id,
            @Param("examDate") LocalDate examDate,
            @Param("sectionId") UUID sectionId,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("""
    SELECT e FROM ExamSchedule e
    WHERE e.id <> :id
      AND e.examDate = :examDate
      AND e.roomNumber = :roomNumber
      AND e.startTime < :endTime
      AND e.endTime > :startTime
""")
    List<ExamSchedule> findRoomConflicts(
            @Param("id") UUID id,
            @Param("examDate") LocalDate examDate,
            @Param("roomNumber") String roomNumber,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}