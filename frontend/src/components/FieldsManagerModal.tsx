import { useEffect, useState } from "react";
import { Modal } from "./Modal";
import { ConfirmDialog } from "./ConfirmDialog";
import { ToastStack } from "./Toast";
import { useToasts } from "../hooks/useToasts";
import { entityDefinitionApi } from "../api/platformApi";
import { ApiError } from "../api/client";
import {
  DATA_TYPES,
  type EntityDefinition,
  type FieldDefinition,
  type FieldDefinitionRequest,
} from "../types/platform";

interface FieldsManagerModalProps {
  entityDefinition: EntityDefinition;
  allEntityDefinitions: EntityDefinition[];
  onClose: () => void;
}

const EMPTY_FORM: FieldDefinitionRequest = {
  name: "",
  label: "",
  dataType: "TEXT",
  required: false,
  enumOptions: [],
  displayOrder: 0,
};

export function FieldsManagerModal({
  entityDefinition,
  allEntityDefinitions,
  onClose,
}: FieldsManagerModalProps) {
  const [fields, setFields] = useState<FieldDefinition[]>([]);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState<FieldDefinition | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState<FieldDefinitionRequest>(EMPTY_FORM);
  const [enumOptionsText, setEnumOptionsText] = useState("");
  const [deleteTarget, setDeleteTarget] = useState<FieldDefinition | null>(null);
  const [saving, setSaving] = useState(false);
  const toasts = useToasts();

  const load = async () => {
    setLoading(true);
    try {
      setFields(await entityDefinitionApi.listFields(entityDefinition.id));
    } catch (err) {
      toasts.error(err instanceof ApiError ? err.message : "Failed to load fields");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const openCreate = () => {
    setEditing(null);
    setForm({ ...EMPTY_FORM, displayOrder: fields.length });
    setEnumOptionsText("");
    setShowForm(true);
  };

  const openEdit = (field: FieldDefinition) => {
    setEditing(field);
    setForm({
      name: field.name,
      label: field.label,
      dataType: field.dataType,
      required: field.required,
      referenceTargetEntityDefinitionId: field.referenceTargetEntityDefinitionId,
      enumOptions: field.enumOptions,
      displayOrder: field.displayOrder,
    });
    setEnumOptionsText((field.enumOptions ?? []).join(", "));
    setShowForm(true);
  };

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    try {
      const payload: FieldDefinitionRequest = {
        ...form,
        enumOptions:
          form.dataType === "ENUM"
            ? enumOptionsText
                .split(",")
                .map((s) => s.trim())
                .filter(Boolean)
            : undefined,
        referenceTargetEntityDefinitionId:
          form.dataType === "REFERENCE" ? form.referenceTargetEntityDefinitionId : undefined,
      };

      if (editing) {
        await entityDefinitionApi.updateField(entityDefinition.id, editing.id, payload);
        toasts.success("Field updated");
      } else {
        await entityDefinitionApi.addField(entityDefinition.id, payload);
        toasts.success("Field added");
      }
      setShowForm(false);
      await load();
    } catch (err) {
      toasts.error(err instanceof ApiError ? err.message : "Save failed");
    } finally {
      setSaving(false);
    }
  };

  const confirmDelete = async () => {
    if (!deleteTarget) return;
    try {
      await entityDefinitionApi.removeField(entityDefinition.id, deleteTarget.id);
      toasts.success("Field deleted");
      setDeleteTarget(null);
      await load();
    } catch (err) {
      toasts.error(err instanceof ApiError ? err.message : "Delete failed");
    }
  };

  const otherDefinitions = allEntityDefinitions.filter((d) => d.id !== entityDefinition.id);

  return (
    <Modal title={`Fields — ${entityDefinition.pluralLabel}`} onClose={onClose}>
      <ToastStack toasts={toasts.toasts} onDismiss={toasts.dismiss} />

      {!showForm ? (
        <div>
          {loading ? (
            <p className="form-hint">Loading…</p>
          ) : fields.length === 0 ? (
            <p className="form-hint">No fields yet — add one below.</p>
          ) : (
            <table className="table">
              <thead>
                <tr>
                  <th>Label</th>
                  <th>Name</th>
                  <th>Type</th>
                  <th>Required</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {fields
                  .slice()
                  .sort((a, b) => a.displayOrder - b.displayOrder)
                  .map((f) => (
                    <tr key={f.id}>
                      <td className="cell-strong">{f.label}</td>
                      <td className="cell-mono">{f.name}</td>
                      <td>{f.dataType}</td>
                      <td>{f.required ? "Yes" : "—"}</td>
                      <td className="cell-actions">
                        <button
                          type="button"
                          className="btn btn--ghost btn--sm"
                          onClick={() => openEdit(f)}
                        >
                          Edit
                        </button>
                        <button
                          type="button"
                          className="btn btn--ghost btn--sm btn--danger-text"
                          onClick={() => setDeleteTarget(f)}
                        >
                          Delete
                        </button>
                      </td>
                    </tr>
                  ))}
              </tbody>
            </table>
          )}

          <div className="form-actions">
            <button type="button" className="btn btn--ghost" onClick={onClose}>
              Close
            </button>
            <button type="button" className="btn btn--primary" onClick={openCreate}>
              + New Field
            </button>
          </div>
        </div>
      ) : (
        <form onSubmit={submit} className="form">
          <div className="form-row">
            <label>
              Field Name (key in data)
              <input
                required
                value={form.name}
                onChange={(e) => setForm({ ...form, name: e.target.value })}
                placeholder="e.g. firstName"
              />
            </label>
            <label>
              Label (shown in UI)
              <input
                required
                value={form.label}
                onChange={(e) => setForm({ ...form, label: e.target.value })}
                placeholder="e.g. First Name"
              />
            </label>
          </div>

          <label>
            Data Type
            <select
              value={form.dataType}
              onChange={(e) => setForm({ ...form, dataType: e.target.value as typeof form.dataType })}
            >
              {DATA_TYPES.map((t) => (
                <option key={t} value={t}>
                  {t}
                </option>
              ))}
            </select>
          </label>

          {form.dataType === "ENUM" && (
            <label>
              Options (comma-separated)
              <input
                value={enumOptionsText}
                onChange={(e) => setEnumOptionsText(e.target.value)}
                placeholder="e.g. LOW, MEDIUM, HIGH"
              />
            </label>
          )}

          {form.dataType === "REFERENCE" && (
            <label>
              References Entity
              <select
                required
                value={form.referenceTargetEntityDefinitionId ?? ""}
                onChange={(e) =>
                  setForm({ ...form, referenceTargetEntityDefinitionId: e.target.value })
                }
              >
                <option value="">Select…</option>
                {otherDefinitions.map((d) => (
                  <option key={d.id} value={d.id}>
                    {d.pluralLabel}
                  </option>
                ))}
              </select>
            </label>
          )}

          <label className="checkbox-row">
            <input
              type="checkbox"
              checked={form.required}
              onChange={(e) => setForm({ ...form, required: e.target.checked })}
            />
            Required
          </label>

          <div className="form-actions">
            <button type="button" className="btn btn--ghost" onClick={() => setShowForm(false)}>
              Cancel
            </button>
            <button type="submit" className="btn btn--primary" disabled={saving}>
              {saving ? "Saving…" : "Save Field"}
            </button>
          </div>
        </form>
      )}

      {deleteTarget && (
        <ConfirmDialog
          message={`Delete field "${deleteTarget.label}"? This cannot be undone.`}
          onConfirm={confirmDelete}
          onCancel={() => setDeleteTarget(null)}
        />
      )}
    </Modal>
  );
}
