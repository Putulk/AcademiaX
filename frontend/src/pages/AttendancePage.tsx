import { useState } from "react";
import { attendanceApi } from "../api/attendanceApi";
import {
  loadClassRoomOptions,
  loadSectionOptions,
  loadStudentEnrollmentOptions,
  loadSubjectOptions,
  loadTeacherOptions,
} from "../api/directory";
import { CrudPage, type CrudField, type CrudColumn } from "../components/CrudPage";
import { StatusBadge } from "../components/StatusBadge";
import { MarkAttendance } from "./MarkAttendance";
import {
  ATTENDANCE_STATUSES,
  type Attendance,
  type AttendanceRequest,
} from "../types/attendance";

const columns: CrudColumn<Attendance>[] = [
  { key: "studentEnrollmentId", label: "Student", lookup: loadStudentEnrollmentOptions },
  { key: "subjectId", label: "Subject", lookup: loadSubjectOptions },
  { key: "attendanceDate", label: "Date" },
  { key: "status", label: "Status", render: (item) => <StatusBadge value={item.status} /> },
  { key: "remarks", label: "Remarks" },
];

const fields: CrudField<AttendanceRequest>[] = [
  { key: "studentEnrollmentId", label: "Student Enrollment", type: "reference", loadOptions: loadStudentEnrollmentOptions, required: true },
  { key: "classId", label: "Class", type: "reference", loadOptions: loadClassRoomOptions, required: true },
  { key: "sectionId", label: "Section", type: "reference", loadOptions: loadSectionOptions, required: true },
  { key: "subjectId", label: "Subject", type: "reference", loadOptions: loadSubjectOptions, required: true },
  { key: "teacherId", label: "Teacher", type: "reference", loadOptions: loadTeacherOptions, required: true },
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

function AllAttendanceRecords() {
  return (
    <CrudPage
      title="Attendance"
      subtitle="Every attendance record — use this for one-off corrections or deletions."
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

export function AttendancePage() {
  const [tab, setTab] = useState<"mark" | "all">("mark");

  return (
    <div>
      <div className="page-header">
        <div>
          <h2>Attendance</h2>
          <p className="page-subtitle">
            {tab === "mark"
              ? "Pick a class section and mark everyone's status in one go."
              : "Every attendance record across all sections and subjects."}
          </p>
        </div>
        <div className="filter-bar" style={{ margin: 0 }}>
          <button
            type="button"
            className={`btn ${tab === "mark" ? "btn--primary" : "btn--ghost"}`}
            onClick={() => setTab("mark")}
          >
            Mark Attendance
          </button>
          <button
            type="button"
            className={`btn ${tab === "all" ? "btn--primary" : "btn--ghost"}`}
            onClick={() => setTab("all")}
          >
            All Records
          </button>
        </div>
      </div>

      {tab === "mark" ? <MarkAttendance /> : <AllAttendanceRecords />}
    </div>
  );
}
