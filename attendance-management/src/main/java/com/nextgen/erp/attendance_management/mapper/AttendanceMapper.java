package com.nextgen.erp.attendance_management.mapper;

import com.nextgen.erp.attendance_management.dto.AttendanceRequest;
import com.nextgen.erp.attendance_management.dto.AttendanceResponse;
import com.nextgen.erp.attendance_management.entity.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

    public Attendance toEntity(AttendanceRequest request) {

        return Attendance.builder()
                .studentEnrollmentId(request.getStudentEnrollmentId())
                .classId(request.getClassId())
                .sectionId(request.getSectionId())
                .subjectId(request.getSubjectId())
                .teacherId(request.getTeacherId())
                .attendanceDate(request.getAttendanceDate())
                .status(request.getStatus())
                .remarks(request.getRemarks())
                .build();
    }

    public AttendanceResponse toResponse(Attendance entity) {

        return AttendanceResponse.builder()
                .id(entity.getId())
                .studentEnrollmentId(entity.getStudentEnrollmentId())
                .classId(entity.getClassId())
                .sectionId(entity.getSectionId())
                .subjectId(entity.getSubjectId())
                .teacherId(entity.getTeacherId())
                .attendanceDate(entity.getAttendanceDate())
                .status(entity.getStatus())
                .remarks(entity.getRemarks())
                .build();
    }

    public void updateEntity(Attendance attendance,
                             AttendanceRequest request) {

        attendance.setStudentEnrollmentId(request.getStudentEnrollmentId());
        attendance.setClassId(request.getClassId());
        attendance.setSectionId(request.getSectionId());
        attendance.setSubjectId(request.getSubjectId());
        attendance.setTeacherId(request.getTeacherId());
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setStatus(request.getStatus());
        attendance.setRemarks(request.getRemarks());
    }
}
