import { teacherApi } from "../api/facultyApi";
import { loadUserProfileOptions } from "../api/directory";
import { CrudPage, type CrudField, type CrudColumn } from "../components/CrudPage";
import { StatusBadge } from "../components/StatusBadge";
import type { Teacher, TeacherRequest } from "../types/faculty";

const columns: CrudColumn<Teacher>[] = [
  { key: "employeeCode", label: "Employee Code" },
  { key: "userProfileId", label: "Name", lookup: loadUserProfileOptions },
  { key: "designation", label: "Designation" },
  { key: "department", label: "Department" },
  { key: "joiningDate", label: "Joined" },
  {
    key: "active",
    label: "Status",
    render: (item) => <StatusBadge value={item.active ? "ACTIVE" : "INACTIVE"} />,
  },
];

const fields: CrudField<TeacherRequest>[] = [
  { key: "userProfileId", label: "User Profile", type: "reference", loadOptions: loadUserProfileOptions, required: true },
  { key: "employeeCode", label: "Employee Code", type: "text", required: true },
  { key: "designation", label: "Designation", type: "text" },
  { key: "department", label: "Department", type: "text" },
  { key: "joiningDate", label: "Joining Date", type: "date", required: true },
  { key: "experienceYears", label: "Experience (Years)", type: "number" },
  { key: "salary", label: "Salary", type: "number", step: "0.01" },
  { key: "active", label: "Active", type: "checkbox" },
];

export function TeachersPage() {
  return (
    <CrudPage
      title="Teachers"
      subtitle="Teaching staff records, linked to a user profile."
      api={teacherApi}
      columns={columns}
      fields={fields}
      emptyForm={{
        userProfileId: "",
        employeeCode: "",
        designation: "",
        department: "",
        joiningDate: "",
        experienceYears: 0,
        salary: 0,
        active: true,
      }}
      toRequest={(item) => ({
        userProfileId: item.userProfileId,
        employeeCode: item.employeeCode,
        designation: item.designation ?? "",
        department: item.department ?? "",
        joiningDate: item.joiningDate,
        experienceYears: item.experienceYears ?? 0,
        salary: item.salary ?? 0,
        active: item.active,
      })}
      itemLabel={(item) => item.employeeCode}
    />
  );
}
