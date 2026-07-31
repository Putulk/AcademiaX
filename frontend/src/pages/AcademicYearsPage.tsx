import { academicYearApi } from "../api/academicApi";
import { ApiError } from "../api/client";
import { CrudPage, type CrudField, type CrudColumn } from "../components/CrudPage";
import { StatusBadge } from "../components/StatusBadge";
import type { AcademicYear, AcademicYearRequest } from "../types/academic";

const columns: CrudColumn<AcademicYear>[] = [
  { key: "name", label: "Name" },
  { key: "startDate", label: "Start" },
  { key: "endDate", label: "End" },
  {
    key: "active",
    label: "Status",
    render: (item) => <StatusBadge value={item.active ? "ACTIVE" : "INACTIVE"} />,
  },
];

const fields: CrudField<AcademicYearRequest>[] = [
  { key: "name", label: "Name", type: "text", required: true, placeholder: "2026-2027" },
  { key: "startDate", label: "Start Date", type: "date", required: true },
  { key: "endDate", label: "End Date", type: "date", required: true },
  { key: "active", label: "Active", type: "checkbox" },
];

export function AcademicYearsPage() {
  return (
    <CrudPage
      title="Academic Years"
      subtitle="Academic year windows that classes and enrollments attach to."
      api={academicYearApi}
      columns={columns}
      fields={fields}
      emptyForm={{ name: "", startDate: "", endDate: "", active: false }}
      toRequest={(item) => ({
        name: item.name,
        startDate: item.startDate,
        endDate: item.endDate,
        active: item.active,
      })}
      itemLabel={(item) => item.name}
      extraActions={(item, reload, toasts) => (
        <button
          type="button"
          className="btn btn--ghost btn--sm"
          disabled={item.active}
          onClick={async () => {
            try {
              await academicYearApi.activate(item.id);
              toasts.success(`${item.name} activated`);
              await reload();
            } catch (err) {
              toasts.error(err instanceof ApiError ? err.message : "Activation failed");
            }
          }}
        >
          Activate
        </button>
      )}
    />
  );
}
