import { useEffect, useState } from "react";
import { authApi } from "../api/authApi";
import { ApiError } from "../api/client";
import { RoleAssignModal } from "../components/RoleAssignModal";
import { StatusBadge } from "../components/StatusBadge";
import { ToastStack } from "../components/Toast";
import { useToasts } from "../hooks/useToasts";
import type { UserSummary } from "../types/auth";

export function UsersRolesPage() {
  const [users, setUsers] = useState<UserSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [managing, setManaging] = useState<UserSummary | null>(null);
  const toasts = useToasts();

  const load = async () => {
    setLoading(true);
    try {
      setUsers(await authApi.listUsers());
    } catch (err) {
      toasts.error(
        err instanceof ApiError
          ? err.message
          : "Failed to load users — you may need to be logged in as an admin",
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <div>
      <ToastStack toasts={toasts.toasts} onDismiss={toasts.dismiss} />

      <div className="page-header">
        <div>
          <h2>Users &amp; Roles</h2>
          <p className="page-subtitle">
            Every registered account and their roles. Requires an ADMIN or
            SUPER_ADMIN login — the default seeded account is
            admin@academiax.local / Admin@123.
          </p>
        </div>
      </div>

      <div className="card">
        {loading ? (
          <div className="empty-state">Loading…</div>
        ) : users.length === 0 ? (
          <div className="empty-state">No users found.</div>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>Username</th>
                <th>Name</th>
                <th>Email</th>
                <th>Roles</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {users.map((u) => (
                <tr key={u.id}>
                  <td className="cell-strong">{u.username}</td>
                  <td>
                    {u.firstName} {u.lastName}
                  </td>
                  <td>{u.email}</td>
                  <td>
                    <div className="badge-group">
                      {u.roles.map((role) => (
                        <StatusBadge key={role} value={role} tone="info" />
                      ))}
                    </div>
                  </td>
                  <td className="cell-actions">
                    <button
                      type="button"
                      className="btn btn--ghost btn--sm"
                      onClick={() => setManaging(u)}
                    >
                      Manage Roles
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {managing && (
        <RoleAssignModal
          userId={managing.id}
          displayName={`${managing.firstName} ${managing.lastName}`}
          onClose={() => setManaging(null)}
          onSaved={load}
        />
      )}
    </div>
  );
}
