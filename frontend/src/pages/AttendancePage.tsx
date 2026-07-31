import { attendanceApi } from "../api/attendanceApi";
import { CrudPage, type CrudField, type CrudColumn } from "../components/CrudPage";
import { StatusBadge } from "../components/StatusBadge";
import {
  ATTENDANCE_STATUSES,
  type Attendance,
  type AttendanceRequest,
} from "../types/attendance";

const columns: CrudColumn<Attendance>[] = [
  { key: "studentEnrollmentId", label: "Student Enrollment ID", mono: true },
  { key: "subjectId", label: "Subject ID", mono: true },
  { key: "attendanceDate", label: "Date" },
  { key: "status", label: "Status", render: (item) => <StatusBadge value={item.status} /> },
  { key: "remarks", label: "Remarks" },
];

const fields: CrudField<AttendanceRequest>[] = [
  { key: "studentEnrollmentId", label: "Student Enrollment ID (UUID)", type: "text", required: true },
  { key: "classId", label: "Class ID (UUID)", type: "text", required: true },
  { key: "sectionId", label: "Section ID (UUID)", type: "text", required: true },
  { key: "subjectId", label: "Subject ID (UUID)", type: "text", required: true },
  { key: "teacherId", label: "Teacher ID (UUID)", type: "text", required: true },
  { key: "attendanceDate", label: "Date", type: "date", required: true },
  { key: "status", label: "Status", type: "select", options: ATTENDANCE_STATUSES, required: true },
  { key: "remarks", label: "Remarks", type: "textarea" },
];

const emptyForm: AttendanceRequest = {
  studentEnrollmentId: "",
  classId: "",
  sectionId: "",
  subjectId: "",
  teacherId: "",
  attendanceDate: "",
  status: "PRESENT",
  remarks: "",
};

export function AttendancePage() {
  return (
    <CrudPage
      title="Attendance"
      subtitle="Daily per-subject attendance for a student's enrollment."
      api={attendanceApi}
      columns={columns}
      fields={fields}
      emptyForm={emptyForm}
      toRequest={(item) => ({
        studentEnrollmentId: item.studentEnrollmentId,
        classId: item.classId,
        sectionId: item.sectionId,
        subjectId: item.subjectId,
        teacherId: item.teacherId,
        attendanceDate: item.attendanceDate,
        status: item.status,
        remarks: item.remarks ?? "",
      })}
      itemLabel={(item) => `${item.attendanceDate} — ${item.studentEnrollmentId}`}
    />
  );
}
