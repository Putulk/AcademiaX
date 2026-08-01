import { teacherAssignmentApi } from "../api/facultyApi";
import {
  loadAcademicYearOptions,
  loadClassSectionOptions,
  loadSubjectOptions,
  loadTeacherOptions,
} from "../api/directory";
import { CrudPage, type CrudField, type CrudColumn } from "../components/CrudPage";
import { StatusBadge } from "../components/StatusBadge";
import type { TeacherAssignment, TeacherAssignmentRequest } from "../types/faculty";

const columns: CrudColumn<TeacherAssignment>[] = [
  { key: "teacherId", label: "Teacher", lookup: loadTeacherOptions },
  { key: "classSectionId", label: "Class Section", lookup: loadClassSectionOptions },
  { key: "subjectId", label: "Subject", lookup: loadSubjectOptions },
  { key: "academicYearId", label: "Academic Year", lookup: loadAcademicYearOptions },
  {
    key: "active",
    label: "Status",
    render: (item) => <StatusBadge value={item.active ? "ACTIVE" : "INACTIVE"} />,
  },
];

const fields: CrudField<TeacherAssignmentRequest>[] = [
  { key: "teacherId", label: "Teacher", type: "reference", loadOptions: loadTeacherOptions, required: true },
  { key: "classSectionId", label: "Class Section", type: "reference", loadOptions: loadClassSectionOptions, required: true },
  { key: "subjectId", label: "Subject", type: "reference", loadOptions: loadSubjectOptions, required: true },
  { key: "academicYearId", label: "Academic Year", type: "reference", loadOptions: loadAcademicYearOptions, required: true },
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
