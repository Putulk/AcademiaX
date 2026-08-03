# AcademiaX — Admin Frontend

A React + TypeScript + Vite admin UI covering every resource in the AcademiaX backend, so you can create/edit/delete data and test cross-service flows without Postman/curl. The app opens on a Login/Register screen (`/login`); everything else is gated behind a successful login, and what you actually *see* after logging in depends on your account's role.

See [`DESIGN.md`](./DESIGN.md) for the full UI/UX design write-up (visual system, IA, role matrix, page-by-page notes). This file covers setup and the technical decisions behind how it talks to the backend.

## Prerequisites

- Node.js 20+
- Whichever backend services you want to exercise, running locally (see ports below) and registered with Eureka if you're testing cross-service validation (e.g. creating an Exam Result validates the student enrollment and subject against their owning services via Feign).

## Getting started

```bash
cd frontend
npm install
npm run dev
```

Runs at `http://localhost:3000`. You'll land on `/login`. Either register a new account (always created as `ROLE_STUDENT`) or use the seeded default admin: `admin@academiax.local` / `Admin@123`.

## Login gate and roles

`src/auth/AuthContext.tsx` holds the access token + user (id/username/email/roles) in React state, persisted to `localStorage` (`accessToken`, `authUser`) so a refresh doesn't kick you back to `/login`. `src/auth/RouteGuards.tsx` has the guards used in `App.tsx`:
- `ProtectedLayout` — wraps every real page; redirects to `/login` if there's no token.
- `PublicOnlyRoute` — wraps `/login`; redirects you back in if you're already logged in.
- `SectionGuard` — wraps each module's routes; redirects to `/no-access` if your role(s) don't grant that section (see `src/auth/permissions.ts` for the role → section table, and `DESIGN.md` for the reasoning).
- `HomeRedirect` — the index route; sends you to the first section your role can see, or `/no-access` if none.

This is a **frontend-only** gate: the token comes from a real `auth-service` login, and the API client (`src/api/client.ts`) does attach it as `Authorization: Bearer <token>` on every request — but only `auth-service`'s own admin endpoints (`GET /api/v1/auth/users`, `GET /api/v1/auth/roles`, `PUT /api/v1/auth/users/{id}/roles`) actually check it. None of the six business services validate the token themselves, so nothing stops a direct API call from bypassing the role gate entirely — it controls what the UI shows, not what the backend allows.

## Architecture note: this UI talks to each service directly, not through the gateway

Each module has its own base URL (see `.env.example` / `src/api/serviceUrls.ts`), and each backend service has its own `CorsConfig` allowing `localhost:3000`/`5173`.

Why not go through `api-gateway` (port 8080) for everything? Two reasons discovered while building this:
1. Two of the gateway's route predicates don't match the actual controller paths — `academic-management`'s predicate is `Path=/api/v1/academic/**` but its controllers are at `/api/v1/academic-years`, `/api/v1/classes`, etc; `examination-management`'s predicate is `/api/v1/examination/**` but its controllers are at `/api/v1/exams`, etc. Both are unreachable through the gateway as currently configured.
2. The gateway enforces a JWT filter on every route except `/api/v1/auth/**` — but only `auth-service` itself validates that JWT beyond the gateway. Calling services directly, as this app does, sidesteps needing every request to carry a token the target service doesn't check anyway.

## Ports (also in `.env.example`)

| Service | Port |
|---|---|
| auth-service | 8081 |
| user-management | 8082 |
| student-management | 8083 |
| academic-management | 8084 |
| faculty-management | 8085 |
| attendance-management | 8087 |
| examination-management | 8088 |

## Project structure

```
src/
  api/         one client file per module, all built on the shared createApiClient() factory;
               directory.ts does the cross-service joins that turn a raw UUID into a real name
  auth/        AuthContext (token/role state), RouteGuards, permissions.ts (role → section table)
  types/       TypeScript interfaces mirroring each backend DTO exactly
  components/  Layout (role-filtered sidebar), CrudPage (generic list+form+delete for most
               resources, with dropdown "reference" fields/columns), Modal, ConfirmDialog,
               StatusBadge, Toast, RoleAssignModal
  hooks/       useToasts — lightweight success/error notification queue
  pages/       one page per resource, plus MarkAttendance.tsx (bulk section-wise attendance)
```

Most pages are a thin config object (columns + form fields) passed into the shared `CrudPage` component — look at `AcademicYearsPage.tsx` for the simplest example. A few needed bespoke layouts instead of the generic pattern: `ExamResultsPage`/`ExamSchedulesPage`/`ExamsPage` (predate `CrudPage`), `AuthPage` (login/register, not tabular), `UsersRolesPage` (role assignment table), and `MarkAttendance` (bulk roster entry, not one-record-at-a-time).

## Known backend inconsistencies this UI works around

- Some endpoints wrap responses in `{success, message, data}`, others return the DTO/list raw (e.g. `AcademicYear`, `ClassRoom`, `Student` GET/PUT). The shared API client (`src/api/client.ts`) auto-detects which shape it got.
- `UserProfileResponse` has no `id` field — everything is keyed by `userId`. The frontend aliases `id = userId` locally so it fits the same generic list/edit/delete pattern as everything else.
- Registering a new user via Auth always assigns `ROLE_STUDENT` — there's no way to register as admin/teacher through that endpoint; use Users & Roles (admin-only) to promote an account afterwards.
