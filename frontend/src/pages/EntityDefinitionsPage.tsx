import { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { entityDefinitionApi } from "../api/platformApi";
import { ApiError } from "../api/client";
import { CrudPage, type CrudField, type CrudColumn } from "../components/CrudPage";
import { FieldsManagerModal } from "../components/FieldsManagerModal";
import { StatusBadge } from "../components/StatusBadge";
import { useAuth } from "../auth/AuthContext";
import { useToasts } from "../hooks/useToasts";
import type { EntityDefinition, EntityDefinitionRequest } from "../types/platform";

const columns: CrudColumn<EntityDefinition>[] = [
  { key: "name", label: "Name" },
  { key: "pluralLabel", label: "Plural Label" },
  { key: "displayFieldName", label: "Display Field", mono: true },
  {
    key: "active",
    label: "Status",
    render: (item) => <StatusBadge value={item.active ? "ACTIVE" : "INACTIVE"} />,
  },
];

const fields: CrudField<EntityDefinitionRequest>[] = [
  { key: "name", label: "Name (singular)", type: "text", required: true, placeholder: "e.g. Patient" },
  { key: "pluralLabel", label: "Plural Label", type: "text", required: true, placeholder: "e.g. Patients" },
  { key: "description", label: "Description", type: "textarea" },
  {
    key: "displayFieldName",
    label: "Display Field (which field name shows as the label)",
    type: "text",
    required: true,
    placeholder: "e.g. name",
  },
  { key: "active", label: "Active", type: "checkbox" },
];

const emptyForm: EntityDefinitionRequest = {
  name: "",
  pluralLabel: "",
  description: "",
  displayFieldName: "",
  active: true,
};

export function EntityDefinitionsPage() {
  const auth = useAuth();
  const [allDefinitions, setAllDefinitions] = useState<EntityDefinition[]>([]);
  const [managingFieldsFor, setManagingFieldsFor] = useState<EntityDefinition | null>(null);
  const toasts = useToasts();

  const loadAllDefinitions = async () => {
    try {
      setAllDefinitions(await entityDefinitionApi.list());
    } catch (err) {
      toasts.error(err instanceof ApiError ? err.message : "Failed to load entity definitions");
    }
  };

  useEffect(() => {
    loadAllDefinitions();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  if (!auth.isAdmin) {
    return <Navigate to="/" replace />;
  }

  return (
    <div>
      <CrudPage
        title="Entity Definitions"
        subtitle="Define custom record types (any domain — Patient, Order, Asset, …) and their fields. No code required."
        api={entityDefinitionApi}
        columns={columns}
        fields={fields}
        emptyForm={emptyForm}
        toRequest={(item) => ({
          name: item.name,
          pluralLabel: item.pluralLabel,
          description: item.description ?? "",
          displayFieldName: item.displayFieldName,
          active: item.active,
        })}
        itemLabel={(item) => item.pluralLabel}
        extraActions={(item) => (
          <button
            type="button"
            className="btn btn--ghost btn--sm"
            onClick={() => setManagingFieldsFor(item)}
          >
            Manage Fields
          </button>
        )}
      />

      {managingFieldsFor && (
        <FieldsManagerModal
          entityDefinition={managingFieldsFor}
          allEntityDefinitions={allDefinitions}
          onClose={() => {
            setManagingFieldsFor(null);
            loadAllDefinitions();
          }}
        />
      )}
    </div>
  );
}
