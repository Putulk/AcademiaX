import { useAuth } from "../auth/AuthContext";
import { StatusBadge } from "../components/StatusBadge";

export function NoAccessPage() {
  const auth = useAuth();
  const roles = auth.user?.roles ?? [];

  return (
    <div className="card">
      <div className="empty-state" style={{ padding: "48px 24px" }}>
        <h2 style={{ marginBottom: 10 }}>No modules available for your role yet</h2>
        <p className="page-subtitle" style={{ marginBottom: 16 }}>
          Your account has {roles.length === 0 ? "no roles assigned" : "the role(s) below"},
          and none of them map to a module that exists in this system yet — some
          roles (Accountant, Librarian, HR) are seeded for future modules
          (Fee, Library, HR) that haven't been built.
        </p>
        <div className="badge-group" style={{ justifyContent: "center" }}>
          {roles.map((role) => (
            <StatusBadge key={role} value={role} tone="info" />
          ))}
        </div>
        <p className="page-subtitle" style={{ marginTop: 20 }}>
          Ask an admin to assign you a role like Teacher, Student, or
          Management from the Users &amp; Roles page.
        </p>
      </div>
    </div>
  );
}
