# AcademiaX — Admin Frontend

A React + TypeScript + Vite admin UI for testing the AcademiaX microservices by hand, without Postman/curl. The app opens on a Login/Register screen (`/login`); every other route is gated behind a successful login and only becomes reachable afterwards. It covers every resource that exists in the backend today, organized by module in the sidebar:

- **Users** — `UserProfile` CRUD (`user-management`)
- **Academic** — Academic Years, Classes, Sections, Subjects, Class Sections (`academic-management`)
- **Faculty** — Teachers, Teacher Assignments (`faculty-management`)
- **Students** — Students, Student Enrollments (`student-management`)
- **Attendance** — daily per-subject attendance (`attendance-management`)
- **Examination** — Exams, Exam Schedules, Exam Results (`examination-management`) — grade/pass-fail are computed server-side, never entered manually

## Prerequisites

- Node.js 20+
- Whichever backend services you want to exercise, running locally (see ports below) and registered with Eureka if you're testing cross-service validation (e.g. creating an Exam Result validates the student enrollment and subject against their owning services via Feign).

## Getting started

```bash
cd frontend
npm install
npm run dev
```

Runs at `http://localhost:3000`. You'll land on `/login` — register an account (always created as `ROLE_STUDENT`, see below), then log in to reach the rest of the app.

## Login gate

`src/auth/AuthContext.tsx` holds the access token + user (username/email) in React state, persisted to `localStorage` (`accessToken`, `authUser`) so a refresh doesn't kick you back to `/login`. `src/auth/RouteGuards.tsx` has the two guards used in `App.tsx`:
- `ProtectedLayout` — wraps every real page; redirects to `/login` if there's no token.
- `PublicOnlyRoute` — wraps `/login`; redirects to `/exams` if you're already logged in (so you can't land back on the login screen while authenticated).

This is a **frontend-only** gate, matching the backend as it exists today: the token comes from a real `auth-service` login, but none of the other services actually validate it (see [Known backend inconsistencies](#known-backend-inconsistencies-this-ui-works-around)) — the UI doesn't attach it as an `Authorization` header to the other API calls because nothing checks for one. If that changes (services start enforcing JWT auth), the API client (`src/api/client.ts`) is the one place to add the header.

## Architecture note: this UI talks to each service directly, not through the gateway

Each module has its own base URL (see `.env.example` / `src/api/serviceUrls.ts`), and each backend service now has its own `CorsConfig` allowing `localhost:3000`/`5173` — that's a new addition alongside this frontend, since none of them had CORS configured before.

Why not go through `api-gateway` (port 8080) for everything? Two reasons discovered while building this:
1. Two of the gateway's route predicates don't match the actual controller paths — `academic-management`'s predicate is `Path=/api/v1/academic/**` but its controllers are at `/api/v1/academic-years`, `/api/v1/classes`, etc; `examination-management`'s predicate is `/api/v1/examination/**` but its controllers are at `/api/v1/exams`, etc. Both are unreachable through the gateway as currently configured.
2. The gateway enforces a JWT filter on every route except `/api/v1/auth/**` — but none of the underlying services enforce security themselves (only `auth-service` has Spring Security wired up). Calling services directly, as this app does, sidesteps needing a login flow just to click around and test data.

If you'd rather route everything through the gateway (e.g. to test the real auth flow end-to-end), you'd need to fix those two route predicates and add a JWT `Authorization` header to every request — neither is done here.

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
  api/         one client file per module, all built on the shared createApiClient() factory
  types/       TypeScript interfaces mirroring each backend DTO exactly
  components/  Layout (sidebar/topbar), CrudPage (generic list+form+delete for most resources),
               Modal, ConfirmDialog, StatusBadge, Toast
  hooks/       useToasts — lightweight success/error notification queue
  pages/       one page per resource
```

Most pages are a thin config object (columns + form fields) passed into the shared `CrudPage` component — look at `AcademicYearsPage.tsx` for the simplest example, or `ExamResultsPage.tsx`/`AuthPage.tsx` for the two pages that needed bespoke behavior (computed grade UI, and a non-tabular login/register form) instead of the generic pattern.

## Known backend inconsistencies this UI works around

- Some endpoints wrap responses in `{success, message, data}`, others return the DTO/list raw (e.g. `AcademicYear`, `ClassRoom`, `Student` GET/PUT). The shared API client (`src/api/client.ts`) auto-detects which shape it got.
- `UserProfileResponse` has no `id` field — everything is keyed by `userId`. The frontend aliases `id = userId` locally so it fits the same generic list/edit/delete pattern as everything else.
- Registering a new user via Auth always assigns `ROLE_STUDENT` — there's no way to register as admin/teacher through this endpoint today.
