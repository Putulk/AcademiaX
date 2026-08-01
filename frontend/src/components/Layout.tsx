import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import { getAllowedSections, type SectionKey } from "../auth/permissions";
import { StatusBadge } from "./StatusBadge";

interface NavItem {
  to: string;
  label: string;
  icon: string;
}

interface NavSection {
  heading: SectionKey;
  items: NavItem[];
}

const ALL_NAV_SECTIONS: NavSection[] = [
  {
    heading: "Admin",
    items: [{ to: "/admin/users", label: "Users & Roles", icon: "🛡️" }],
  },
  {
    heading: "Users",
    items: [{ to: "/user-profiles", label: "User Profiles", icon: "🪪" }],
  },
  {
    heading: "Academic",
    items: [
      { to: "/academic-years", label: "Academic Years", icon: "📅" },
      { to: "/classes", label: "Classes", icon: "🏷️" },
      { to: "/sections", label: "Sections", icon: "🔤" },
      { to: "/subjects", label: "Subjects", icon: "📖" },
      { to: "/class-sections", label: "Class Sections", icon: "🧩" },
    ],
  },
  {
    heading: "Faculty",
    items: [
      { to: "/teachers", label: "Teachers", icon: "🧑‍🏫" },
      { to: "/teacher-assignments", label: "Teacher Assignments", icon: "📋" },
    ],
  },
  {
    heading: "Students",
    items: [
      { to: "/students", label: "Students", icon: "🎓" },
      { to: "/student-enrollments", label: "Student Enrollments", icon: "📝" },
    ],
  },
  {
    heading: "Attendance",
    items: [{ to: "/attendance", label: "Attendance", icon: "✅" }],
  },
  {
    heading: "Examination",
    items: [
      { to: "/exams", label: "Exams", icon: "📘" },
      { to: "/schedules", label: "Exam Schedules", icon: "🗓️" },
      { to: "/results", label: "Exam Results", icon: "🏆" },
    ],
  },
];

export function Layout() {
  const auth = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    auth.logout();
    navigate("/login", { replace: true });
  };

  const displayName = auth.user?.username || auth.user?.email || "Signed in";
  const tooltip = auth.user?.id ? `${displayName} — User ID: ${auth.user.id}` : displayName;

  const allowedSections = getAllowedSections(auth.user?.roles);
  const navSections = ALL_NAV_SECTIONS.filter((section) =>
    allowedSections.has(section.heading),
  );

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="sidebar__brand">
          <span className="sidebar__brand-mark">Ax</span>
          <span className="sidebar__brand-name">AcademiaX</span>
        </div>

        <nav className="sidebar__nav">
          {navSections.map((section) => (
            <div key={section.heading} className="sidebar__section">
              <div className="sidebar__heading">{section.heading}</div>
              {section.items.map((item) => (
                <NavLink
                  key={item.to}
                  to={item.to}
                  className={({ isActive }) =>
                    `sidebar__link${isActive ? " sidebar__link--active" : ""}`
                  }
                >
                  <span className="sidebar__icon">{item.icon}</span>
                  {item.label}
                </NavLink>
              ))}
            </div>
          ))}
        </nav>

        <div className="sidebar__footer">
          <div className="sidebar__user" title={tooltip}>
            {displayName}
          </div>
          <div className="badge-group">
            {(auth.user?.roles ?? []).map((role) => (
              <StatusBadge key={role} value={role} tone="neutral" />
            ))}
          </div>
          <button type="button" className="sidebar__logout" onClick={handleLogout}>
            Log out
          </button>
        </div>
      </aside>

      <div className="app-main">
        <header className="topbar">
          <h1>AcademiaX</h1>
        </header>

        <main className="content">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
