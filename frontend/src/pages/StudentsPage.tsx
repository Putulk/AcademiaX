import { studentApi } from "../api/studentApi";
import { loadClassRoomOptions, loadSectionOptions, loadUserProfileOptions } from "../api/directory";
import { CrudPage, type CrudField, type CrudColumn } from "../components/CrudPage";
import { StatusBadge } from "../components/StatusBadge";
import {
  BLOOD_GROUPS,
  RELIGIONS,
  STUDENT_CATEGORIES,
  STUDENT_STATUSES,
  type Student,
  type StudentRequest,
} from "../types/student";

const columns: CrudColumn<Student>[] = [
  { key: "userProfileId", label: "Name", lookup: loadUserProfileOptions },
  { key: "admissionNumber", label: "Admission No." },
  { key: "rollNumber", label: "Roll No." },
  { key: "academicYear", label: "Academic Year" },
  { key: "admissionDate", label: "Admitted" },
  { key: "status", label: "Status", render: (item) => <StatusBadge value={item.status} /> },
];

const fields: CrudField<StudentRequest>[] = [
  { key: "userProfileId", label: "User Profile", type: "reference", loadOptions: loadUserProfileOptions, required: true },
  { key: "classId", label: "Class (optional)", type: "reference", loadOptions: loadClassRoomOptions },
  { key: "sectionId", label: "Section (optional)", type: "reference", loadOptions: loadSectionOptions },
  { key: "admissionNumber", label: "Admission Number", type: "text", required: true },
  { key: "rollNumber", label: "Roll Number", type: "text", required: true },
  { key: "academicYear", label: "Academic Year", type: "text", required: true, placeholder: "2026-2027" },
  { key: "admissionDate", label: "Admission Date", type: "date", required: true },
  { key: "bloodGroup", label: "Blood Group", type: "select", options: BLOOD_GROUPS },
  { key: "religion", label: "Religion", type: "select", options: RELIGIONS },
  { key: "category", label: "Category", type: "select", options: STUDENT_CATEGORIES },
  { key: "house", label: "House", type: "text" },
  { key: "status", label: "Status", type: "select", options: STUDENT_STATUSES, required: true },
];

const emptyForm: StudentRequest = {
  userProfileId: "",
  classId: "",
  sectionId: "",
  admissionNumber: "",
  rollNumber: "",
  academicYear: "",
  admissionDate: "",
  house: "",
  status: "ACTIVE",
};

export function StudentsPage() {
  return (
    <CrudPage
      title="Students"
      subtitle="Student master records, linked to a user profile."
      api={studentApi}
      columns={columns}
      fields={fields}
      emptyForm={emptyForm}
      toRequest={(item) => ({
        userProfileId: item.userProfileId,
        classId: item.classId ?? "",
        sectionId: item.sectionId ?? "",
        admissionNumber: item.admissionNumber,
        rollNumber: item.rollNumber,
        academicYear: item.academicYear,
        admissionDate: item.admissionDate,
        bloodGroup: item.bloodGroup,
        religion: item.religion,
        category: item.category,
        house: item.house ?? "",
        status: item.status,
      })}
      itemLabel={(item) => item.admissionNumber}
    />
  );
}
