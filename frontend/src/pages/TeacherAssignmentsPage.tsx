import { teacherAssignmentApi } from "../api/facultyApi";
import { CrudPage, type CrudField, type CrudColumn } from "../components/CrudPage";
import { StatusBadge } from "../components/StatusBadge";
import type { TeacherAssignment, TeacherAssignmentRequest } from "../types/faculty";

const columns: CrudColumn<TeacherAssignment>[] = [
  { key: "teacherId", label: "Teacher ID", mono: true },
  { key: "classSectionId", label: "Class Section ID", mono: true },
  { key: "subjectId", label: "Subject ID", mono: true },
  { key: "academicYearId", label: "Academic Year ID", mono: true },
  {
    key: "active",
    label: "Status",
    render: (item) => <StatusBadge value={item.active ? "ACTIVE" : "INACTIVE"} />,
  },
];

const fields: CrudField<TeacherAssignmentRequest>[] = [
  { key: "teacherId", label: "Teacher ID (UUID)", type: "text", required: true },
  { key: "classSectionId", label: "Class Section ID (UUID)", type: "text", required: true },
  { key: "subjectId", label: "Subject ID (UUID)", type: "text", required: true },
  { key: "academicYearId", label: "Academic Year ID (UUID)", type: "text", required: true },
  { key: "active", label: "Active", type: "checkbox" },
];

export function TeacherAssignmentsPage() {
  return (
    <CrudPage
      title="Teacher Assignments"
      subtitle="Which teacher teaches which subject, to which class section, in which academic year."
      api={teacherAssignmentApi}
      columns={columns}
      fields={fields}
      emptyForm={{
        teacherId: "",
        classSectionId: "",
        subjectId: "",
        academicYearId: "",
        active: true,
      }}
      toRequest={(item) => ({
        teacherId: item.teacherId,
        classSectionId: item.classSectionId,
        subjectId: item.subjectId,
        academicYearId: item.academicYearId,
        active: item.active,
      })}
      itemLabel={(item) => item.id}
    />
  );
}
