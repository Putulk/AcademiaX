export type SectionKey =
  | "Admin"
  | "Users"
  | "Academic"
  | "Faculty"
  | "Students"
  | "Attendance"
  | "Examination";

// Where "Home" (the index route) should send a user for each section —
// used to land them somewhere real instead of a hardcoded page.
export const SECTION_HOME: Record<SectionKey, string> = {
  Admin: "/admin/users",
  Users: "/user-profiles",
  Academic: "/academic-years",
  Faculty: "/teachers",
  Students: "/students",
  Attendance: "/attendance",
  Examination: "/exams",
};

// Which sections each role can see. Only modules that actually exist in the
// backend are listed anywhere here — ROLE_ACCOUNTANT/ROLE_LIBRARIAN get an
// empty list because there's no fee/library service to show them yet, and
// ROLE_HR gets Faculty as the closest existing match to "staff management".
// This is a UI-only gate (nothing server-side enforces it) — same caveat as
// the login gate itself.
const ROLE_SECTIONS: Record<string, SectionKey[]> = {
  ROLE_SUPER_ADMIN: [
    "Admin",
    "Users",
    "Academic",
    "Faculty",
    "Students",
    "Attendance",
    "Examination",
  ],
  ROLE_ADMIN: [
    "Admin",
    "Users",
    "Academic",
    "Faculty",
    "Students",
    "Attendance",
    "Examination",
  ],
  ROLE_MANAGEMENT: [
    "Users",
    "Academic",
    "Faculty",
    "Students",
    "Attendance",
    "Examination",
  ],
  ROLE_TEACHER: ["Faculty", "Students", "Attendance", "Examination"],
  ROLE_STUDENT: ["Attendance", "Examination"],
  ROLE_PARENT: ["Attendance", "Examination"],
  ROLE_ACCOUNTANT: [],
  ROLE_LIBRARIAN: [],
  ROLE_HR: ["Faculty"],
};

export function getAllowedSections(roles: string[] | undefined): Set<SectionKey> {
  const allowed = new Set<SectionKey>();

  for (const role of roles ?? []) {
    for (const section of ROLE_SECTIONS[role] ?? []) {
      allowed.add(section);
    }
  }

  return allowed;
}
