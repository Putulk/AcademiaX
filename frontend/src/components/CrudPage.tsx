import { useEffect, useState, type ReactNode } from "react";
import { Modal } from "./Modal";
import { ConfirmDialog } from "./ConfirmDialog";
import { ToastStack } from "./Toast";
import { useToasts } from "../hooks/useToasts";
import { ApiError } from "../api/client";
import type { ReferenceOption } from "../api/directory";

export interface CrudColumn<T> {
  key: string;
  label: string;
  render?: (item: T) => ReactNode;
  mono?: boolean;
  lookup?: () => Promise<ReferenceOption[]>;
}

export interface CrudField<TReq> {
  key: keyof TReq;
  label: string;
  type: "text" | "number" | "date" | "time" | "select" | "reference" | "checkbox" | "textarea";
  options?: string[];
  loadOptions?: () => Promise<ReferenceOption[]>;
  required?: boolean;
  placeholder?: string;
  step?: string;
}

export interface CrudApi<T, TReq> {
  list: () => Promise<T[]>;
  create: (payload: TReq) => Promise<T>;
  update: (id: string, payload: TReq) => Promise<T>;
  remove: (id: string) => Promise<unknown>;
}

interface CrudPageProps<T extends { id: string }, TReq> {
  title: string;
  subtitle: string;
  api: CrudApi<T, TReq>;
  columns: CrudColumn<T>[];
  fields: CrudField<TReq>[];
  emptyForm: TReq;
  toRequest: (item: T) => TReq;
  itemLabel: (item: T) => string;
  extraActions?: (item: T, reload: () => void, toasts: ReturnType<typeof useToasts>) => ReactNode;
}

export function CrudPage<T extends { id: string }, TReq extends object>({
  title,
  subtitle,
  api,
  columns,
  fields,
  emptyForm,
  toRequest,
  itemLabel,
  extraActions,
}: CrudPageProps<T, TReq>) {
  const [items, setItems] = useState<T[]>([]);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState<T | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState<TReq>(emptyForm);
  const [deleteTarget, setDeleteTarget] = useState<T | null>(null);
  const [saving, setSaving] = useState(false);
  const [referenceOptions, setReferenceOptions] = useState<Record<string, ReferenceOption[]>>({});
  const [referencesLoading, setReferencesLoading] = useState(false);
  const toasts = useToasts();

  useEffect(() => {
    // Columns and reference-type fields often look up the same entity (e.g. a
    // "teacherId" column and a "teacherId" reference field both want the
    // teacher directory) — de-dupe by key so it's only fetched once.
    const loaders = new Map<string, () => Promise<ReferenceOption[]>>();

    fields.forEach((f) => {
      if (f.type === "reference" && f.loadOptions) {
        loaders.set(String(f.key), f.loadOptions);
      }
    });
    columns.forEach((c) => {
      if (c.lookup && !loaders.has(c.key)) {
        loaders.set(c.key, c.lookup);
      }
    });

    if (loaders.size === 0) return;

    setReferencesLoading(true);
    const entries = [...loaders.entries()];
    Promise.all(entries.map(([, loadOptions]) => loadOptions()))
      .then((results) => {
        const map: Record<string, ReferenceOption[]> = {};
        entries.forEach(([key], i) => {
          map[key] = results[i];
        });
        setReferenceOptions(map);
      })
      .catch((err) =>
        toasts.error(
          err instanceof ApiError ? err.message : "Failed to load reference lists",
        ),
      )
      .finally(() => setReferencesLoading(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const load = async () => {
    setLoading(true);
    try {
      setItems(await api.list());
    } catch (err) {
      toasts.error(err instanceof ApiError ? err.message : `Failed to load ${title.toLowerCase()}`);
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
    setForm(emptyForm);
    setShowForm(true);
  };

  const openEdit = (item: T) => {
    setEditing(item);
    setForm(toRequest(item));
    setShowForm(true);
  };

  const setField = (key: keyof TReq, value: unknown) => {
    setForm((prev) => ({ ...prev, [key]: value }));
  };

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    try {
      if (editing) {
        await api.update(editing.id, form);
        toasts.success("Updated");
      } else {
        await api.create(form);
        toasts.success("Created");
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
      await api.remove(deleteTarget.id);
      toasts.success("Deleted");
      setDeleteTarget(null);
      await load();
    } catch (err) {
      toasts.error(err instanceof ApiError ? err.message : "Delete failed");
    }
  };

  return (
    <div>
      <ToastStack toasts={toasts.toasts} onDismiss={toasts.dismiss} />

      <div className="page-header">
        <div>
          <h2>{title}</h2>
          <p className="page-subtitle">{subtitle}</p>
        </div>
        <button type="button" className="btn btn--primary" onClick={openCreate}>
          + New
        </button>
      </div>

      <div className="card">
        {loading ? (
          <div className="empty-state">Loading…</div>
        ) : items.length === 0 ? (
          <div className="empty-state">Nothing here yet. Create one to get started.</div>
        ) : (
          <table className="table">
            <thead>
              <tr>
                {columns.map((col) => (
                  <th key={col.key}>{col.label}</th>
                ))}
                <th></th>
              </tr>
            </thead>
            <tbody>
              {items.map((item) => (
                <tr key={item.id}>
                  {columns.map((col) => {
                    const rawValue = (item as Record<string, unknown>)[col.key];
                    const resolved =
                      col.lookup &&
                      referenceOptions[col.key]?.find((o) => o.value === rawValue)?.label;

                    return (
                      <td key={col.key} className={col.mono && !resolved ? "cell-mono" : undefined}>
                        {col.render
                          ? col.render(item)
                          : (resolved ?? String(rawValue ?? "—"))}
                      </td>
                    );
                  })}
                  <td className="cell-actions">
                    {extraActions?.(item, load, toasts)}
                    <button
                      type="button"
                      className="btn btn--ghost btn--sm"
                      onClick={() => openEdit(item)}
                    >
                      Edit
                    </button>
                    <button
                      type="button"
                      className="btn btn--ghost btn--sm btn--danger-text"
                      onClick={() => setDeleteTarget(item)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {showForm && (
        <Modal title={editing ? `Edit ${title}` : `New ${title}`} onClose={() => setShowForm(false)}>
          <form onSubmit={submit} className="form">
            {fields.map((field) => (
              <label key={String(field.key)} className={field.type === "checkbox" ? "checkbox-row" : undefined}>
                {field.type === "checkbox" ? null : field.label}
                {field.type === "select" ? (
                  <select
                    required={field.required}
                    value={(form[field.key] as string) ?? ""}
                    onChange={(e) => setField(field.key, e.target.value)}
                  >
                    {!field.required && <option value="">—</option>}
                    {field.options?.map((opt) => (
                      <option key={opt} value={opt}>
                        {opt}
                      </option>
                    ))}
                  </select>
                ) : field.type === "reference" ? (
                  <select
                    required={field.required}
                    value={(form[field.key] as string) ?? ""}
                    onChange={(e) => setField(field.key, e.target.value)}
                    disabled={referencesLoading}
                  >
                    <option value="">
                      {referencesLoading ? "Loading…" : !field.required ? "—" : "Select…"}
                    </option>
                    {(referenceOptions[String(field.key)] ?? []).map((opt) => (
                      <option key={opt.value} value={opt.value}>
                        {opt.label}
                      </option>
                    ))}
                  </select>
                ) : field.type === "checkbox" ? (
                  <>
                    <input
                      type="checkbox"
                      checked={Boolean(form[field.key])}
                      onChange={(e) => setField(field.key, e.target.checked)}
                    />
                    {field.label}
                  </>
                ) : field.type === "textarea" ? (
                  <textarea
                    required={field.required}
                    rows={3}
                    value={(form[field.key] as string) ?? ""}
                    onChange={(e) => setField(field.key, e.target.value)}
                    placeholder={field.placeholder}
                  />
                ) : (
                  <input
                    required={field.required}
                    type={field.type}
                    step={field.step}
                    value={(form[field.key] as string | number) ?? (field.type === "number" ? 0 : "")}
                    onChange={(e) =>
                      setField(
                        field.key,
                        field.type === "number" ? Number(e.target.value) : e.target.value,
                      )
                    }
                    placeholder={field.placeholder}
                  />
                )}
              </label>
            ))}

            <div className="form-actions">
              <button type="button" className="btn btn--ghost" onClick={() => setShowForm(false)}>
                Cancel
              </button>
              <button type="submit" className="btn btn--primary" disabled={saving}>
                {saving ? "Saving…" : "Save"}
              </button>
            </div>
          </form>
        </Modal>
      )}

      {deleteTarget && (
        <ConfirmDialog
          message={`Delete "${itemLabel(deleteTarget)}"? This cannot be undone.`}
          onConfirm={confirmDelete}
          onCancel={() => setDeleteTarget(null)}
        />
      )}
    </div>
  );
}
