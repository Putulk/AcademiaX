import { subjectApi } from "../api/academicApi";
import { CrudPage, type CrudField, type CrudColumn } from "../components/CrudPage";
import { StatusBadge } from "../components/StatusBadge";
import { SUBJECT_TYPES, type Subject, type SubjectRequest } from "../types/academic";

const columns: CrudColumn<Subject>[] = [
  { key: "name", label: "Name" },
  { key: "code", label: "Code", mono: true },
  { key: "type", label: "Type", render: (item) => <StatusBadge value={item.type} /> },
  {
    key: "active",
    label: "Status",
    render: (item) => <StatusBadge value={item.active ? "ACTIVE" : "INACTIVE"} />,
  },
];

const fields: CrudField<SubjectRequest>[] = [
  { key: "name", label: "Name", type: "text", required: true, placeholder: "Mathematics" },
  { key: "code", label: "Code", type: "text", required: true, placeholder: "MATH101" },
  { key: "type", label: "Type", type: "select", options: SUBJECT_TYPES, required: true },
  { key: "active", label: "Active", type: "checkbox" },
];

const emptyForm: SubjectRequest = { name: "", code: "", type: "CORE", active: true };

export function SubjectsPage() {
  return (
    <CrudPage
      title="Subjects"
      subtitle="Subjects taught across the school, referenced by exams and schedules."
      api={subjectApi}
      columns={columns}
      fields={fields}
      emptyForm={emptyForm}
      toRequest={(item) => ({
        name: item.name,
        code: item.code,
        type: item.type,
        active: item.active,
      })}
      itemLabel={(item) => item.name}
    />
  );
}
