import { studentEnrollmentApi } from "../api/studentApi";
import { loadAcademicYearOptions, loadClassSectionOptions, loadStudentOptions } from "../api/directory";
import { CrudPage, type CrudField, type CrudColumn } from "../components/CrudPage";
import { StatusBadge } from "../components/StatusBadge";
import {
  STUDENT_STATUSES,
  type StudentEnrollment,
  type StudentEnrollmentRequest,
} from "../types/student";

const columns: CrudColumn<StudentEnrollment>[] = [
  { key: "studentId", label: "Student", lookup: loadStudentOptions },
  { key: "academicYearId", label: "Academic Year", lookup: loadAcademicYearOptions },
  { key: "classSectionId", label: "Class Section", lookup: loadClassSectionOptions },
  { key: "rollNumber", label: "Roll No." },
  { key: "status", label: "Status", render: (item) => <StatusBadge value={item.status} /> },
];

const fields: CrudField<StudentEnrollmentRequest>[] = [
  { key: "studentId", label: "Student", type: "reference", loadOptions: loadStudentOptions, required: true },
  { key: "academicYearId", label: "Academic Year", type: "reference", loadOptions: loadAcademicYearOptions, required: true },
  { key: "classSectionId", label: "Class Section", type: "reference", loadOptions: loadClassSectionOptions, required: true },
  { key: "rollNumber", label: "Roll Number", type: "text", required: true },
  { key: "status", label: "Status", type: "select", options: STUDENT_STATUSES, required: true },
  { key: "active", label: "Active", type: "checkbox" },
];

const emptyForm: StudentEnrollmentRequest = {
  studentId: "",
  academicYearId: "",
  classSectionId: "",
  rollNumber: "",
  status: "ACTIVE",
  active: true,
};

export function StudentEnrollmentsPage() {
  return (
    <CrudPage
      title="Student Enrollments"
      subtitle="A student's enrollment into a class section for an academic year — this is the ID that attendance and exam results reference."
      api={studentEnrollmentApi}
      columns={columns}
      fields={fields}
      emptyForm={emptyForm}
      toRequest={(item) => ({
        studentId: item.studentId,
        academicYearId: item.academicYearId,
        classSectionId: item.classSectionId,
        rollNumber: item.rollNumber,
        status: item.status,
        active: item.active,
      })}
      itemLabel={(item) => item.rollNumber}
    />
  );
}
