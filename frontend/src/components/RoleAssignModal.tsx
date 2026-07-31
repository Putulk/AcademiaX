import { useEffect, useState } from "react";
import { Modal } from "./Modal";
import { ToastStack } from "./Toast";
import { useToasts } from "../hooks/useToasts";
import { authApi } from "../api/authApi";
import { ApiError } from "../api/client";

interface RoleAssignModalProps {
  userId: string;
  displayName: string;
  onClose: () => void;
  onSaved?: () => void;
}

export function RoleAssignModal({
  userId,
  displayName,
  onClose,
  onSaved,
}: RoleAssignModalProps) {
  const [allRoles, setAllRoles] = useState<string[]>([]);
  const [selected, setSelected] = useState<Set<string>>(new Set());
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const toasts = useToasts();

  useEffect(() => {
    (async () => {
      setLoading(true);
      try {
        const [roles, users] = await Promise.all([
          authApi.listRoles(),
          authApi.listUsers(),
        ]);
        setAllRoles(roles);
        const match = users.find((u) => u.id === userId);
        setSelected(new Set(match?.roles ?? []));
      } catch (err) {
        toasts.error(
          err instanceof ApiError
            ? err.message
            : "Failed to load roles — you may need to be logged in as an admin",
        );
      } finally {
        setLoading(false);
      }
    })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [userId]);

  const toggle = (role: string) => {
    setSelected((prev) => {
      const next = new Set(prev);
      if (next.has(role)) {
        next.delete(role);
      } else {
        next.add(role);
      }
      return next;
    });
  };

  const save = async () => {
    setSaving(true);
    try {
      await authApi.assignRoles(userId, Array.from(selected));
      toasts.success("Roles updated");
      onSaved?.();
      onClose();
    } catch (err) {
      toasts.error(err instanceof ApiError ? err.message : "Failed to assign roles");
    } finally {
      setSaving(false);
    }
  };

  return (
    <Modal title={`Assign Roles — ${displayName}`} onClose={onClose}>
      <ToastStack toasts={toasts.toasts} onDismiss={toasts.dismiss} />

      {loading ? (
        <p className="form-hint">Loading roles…</p>
      ) : (
        <div className="form">
          {allRoles.map((role) => (
            <label key={role} className="checkbox-row">
              <input
                type="checkbox"
                checked={selected.has(role)}
                onChange={() => toggle(role)}
              />
              {role}
            </label>
          ))}

          <div className="form-actions">
            <button type="button" className="btn btn--ghost" onClick={onClose}>
              Cancel
            </button>
            <button type="button" className="btn btn--primary" onClick={save} disabled={saving}>
              {saving ? "Saving…" : "Save Roles"}
            </button>
          </div>
        </div>
      )}
    </Modal>
  );
}
