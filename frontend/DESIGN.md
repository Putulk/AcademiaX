# AcademiaX Frontend — UI/UX Design

This documents the design decisions behind the admin frontend: the visual system, the information architecture, the role-based access model, and the interaction patterns used across pages. For setup/run instructions see [`README.md`](./README.md).

## Design goals

1. **One consistent pattern, not sixteen bespoke screens.** Every resource (Academic Years, Teachers, Students, Attendance, …) follows the same list → modal form → confirm-delete shape, so learning one page teaches you all of them.
2. **Select from real data, never paste a UUID.** Every foreign key in every form is a dropdown showing the actual name/label, resolved through whatever cross-service joins are needed — not a raw ID text box.
3. **Show people what their role is for.** The sidebar and routes reflect what a logged-in account can actually do, rather than exposing every admin screen to every account.
4. **Be honest about the gaps.** Where the backend doesn't support something yet (per-student "my own records," Fee/Library/HR modules), the UI says so plainly instead of faking it.

## Visual system

- **Layout:** fixed dark sidebar (240px) + light content area. Sidebar groups pages under section headings that match the role-visibility model below.
- **Color:** a small semantic palette — `--color-primary` (indigo, actions), and five status tones (`neutral`/`info`/`warning`/`success`/`danger`) used consistently for both exam grades and lifecycle statuses (`DRAFT`→neutral, `PUBLISHED`→success, `CANCELLED`→danger, etc. — see `StatusBadge.tsx`'s tone map). Light/dark are both supported via `prefers-color-scheme`.
- **Typography/spacing:** system font stack, one `--radius` (10px) and one card shadow reused everywhere (`.card`), so every page's chrome looks identical regardless of what's inside it.
- **Components reused everywhere:** `Modal` (form container), `ConfirmDialog` (delete confirmation), `StatusBadge` (colored pill for any enum), `Toast`/`useToasts` (success/error notifications), `CrudPage` (see below).

## Information architecture

The sidebar is organized into module-based sections, each mapping to one backend service:

| Section | Pages | Backend service |
|---|---|---|
| Admin | Users & Roles | auth-service |
| Users | User Profiles | user-management |
| Academic | Academic Years, Classes, Sections, Subjects, Class Sections | academic-management |
| Faculty | Teachers, Teacher Assignments | faculty-management |
| Students | Students, Student Enrollments | student-management |
| Attendance | Mark Attendance, All Records | attendance-management |
| Examination | Exams, Exam Schedules, Exam Results | examination-management |

**Which sections a given user sees is role-driven**, not fixed. `src/auth/permissions.ts` maps each of the 9 backend roles to the sections it can access, and a user's *visible* set is the union across however many roles they hold:

| Role | Sections |
|---|---|
| Super Admin | Everything |
| Admin | Everything (both can assign roles — see below) |
| Management | Everything except Admin (runs the school day-to-day, doesn't manage system access) |
| Teacher | Faculty, Students, Attendance, Examination |
| Student | Attendance, Examination |
| Parent | Attendance, Examination |
| Accountant | *(none — no Fee module exists yet)* |
| Librarian | *(none — no Library module exists yet)* |
| HR | Faculty *(closest existing match to "staff management")* |

A user with no accessible section (or zero roles) lands on `NoAccessPage` instead of a broken empty dashboard — it explains plainly that Accountant/Librarian/HR are seeded roles for modules that don't exist in the backend yet.

This is enforced twice: the sidebar only *renders* links you have access to (`Layout.tsx`), and every route is separately wrapped in a `SectionGuard` (`App.tsx`) so typing a URL directly doesn't bypass it. Both are still **frontend-only** — see the Login gate section in the README for what that does and doesn't protect.

## Navigation flow

```
/login  →  (login success)  →  HomeRedirect  →  first accessible section's home page
                                      ↓ (no accessible section)
                                  /no-access
```

Visiting any protected route while logged out bounces to `/login`; visiting `/login` while already logged in bounces to your home page instead of showing the form again.

## Key interaction patterns

### 1. The generic `CrudPage`

Most resource pages (Academic Years, Teachers, Students, Attendance's "All Records" tab, etc.) are a ~40-line config object — table columns, form fields, an `emptyForm`, and a mapping back from entity → request — fed into one shared component. That component handles: loading state, the "+ New" → modal form → save flow, edit, delete-with-confirmation, and toast feedback. Adding a new field type or a new resource page doesn't require touching this component.

### 2. Reference dropdowns instead of raw UUIDs

Every foreign key field (and every table column showing one) resolves through `src/api/directory.ts`, which fetches the related entity list and — where the entity itself has no human-readable name — joins across services to build one:

- **Direct** (no join needed): Class, Section, Subject, Academic Year all have their own `name`.
- **One join**: Teacher and Student have no name of their own — both are joined against `UserProfile` to show the actual person's name.
- **Two joins**: a Student Enrollment resolves Enrollment → Student → UserProfile to show "Jane Doe — Roll 14" instead of three stacked UUIDs.
- **Three-way join for a compound label**: Class Section combines Academic Year + Class + Section into "Grade 5 - A (2026-2027)".

`CrudField`'s `"reference"` type and `CrudColumn`'s `lookup` both consume these same loader functions and are de-duplicated by field key, so a page needing the same directory for both its table and its form only fetches it once.

### 3. Mark Attendance — bulk, not per-record

Attendance was originally the same one-record-at-a-time CRUD form as everything else, which meant filling out class/section/subject/teacher/date/student *for every single student, every single day* — not workable for the actual use case. It's now a dedicated flow (`MarkAttendance.tsx`): pick Class Section + Subject + Teacher + Date once, the full student roster for that section loads automatically, and each row just needs a status (defaulting to Present, with a "Mark All Present" shortcut for the common case). Saving re-checks for an existing record per student for that date/subject and updates it instead of creating a duplicate. The old per-record form still exists as an "All Records" tab for one-off corrections/deletions.

### 4. Role assignment

`RoleAssignModal` is shared between two entry points — the dedicated Users & Roles admin page, and an "Assign Role" quick action right on the User Profiles page (since assigning a role is a natural next step right after creating someone's profile). Both call the same admin-gated `auth-service` endpoints.

### 5. Generic entities — a page that writes itself

`EntityDefinitionsPage` (admin-only) lets you define a brand-new record type — name, fields, types, which one is `REFERENCE`/`ENUM` — entirely from the UI, backed by the new `platform-core` service. `EntityRecordsPage` then does something the other 16 pages don't: it fetches those field definitions at runtime and feeds them into the *same* `CrudPage` component everything else uses, instead of a hardcoded config. Define "Patient" with a `name`/`age`/`admittedOn`, and it immediately has a full list/create/edit/delete screen — no new React file, no new Java entity. The sidebar's "Records" section is populated the same dynamic way. This is the first piece of turning AcademiaX from an education-specific app into a domain-agnostic one — see the root `README.md`'s "The platform layer" section for the backend side and the phased roadmap.

## Known limitations

- **The role gate is UI-only.** It shapes what you see, not what the backend allows — see the README for the security model this reflects.
- **Student/Parent see everyone's data, not "their own."** There's no backend concept yet of "this logged-in user's own student enrollment" or "this parent's child," so Attendance/Examination show the full dataset regardless of who's logged in. Real scoping needs that link modeled server-side first.
- **Accountant, Librarian, HR have little to no UI** — their nominal domains (Fee, Library, HR management) don't exist as backend services yet.
- **Generic entities are single-tenant today.** Every `platform-core` request uses one fixed demo tenant ID (`DEMO_TENANT_ID` in `src/api/platformApi.ts`) — real tenant onboarding/switching is a later phase.
