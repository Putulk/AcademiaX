# AcademiaX

A school/college ERP built as Java Spring Boot microservices, with a React admin frontend for exercising the whole system without Postman — now growing into a **generic, multi-industry platform**: alongside the education-specific services, `platform-core` lets you define entirely new record types (a hospital's "Patient," a retailer's "Order," anything) at runtime, with zero new Java classes or React pages. See "The platform layer" below.

## What's actually built

Nine Spring Boot services, service discovery, and one frontend that talks to all of them:

| Service | Port | Owns |
|---|---|---|
| eureka-server | 8761 | Service discovery |
| api-gateway | 8080 | Routing + JWT filter (see caveats below) |
| auth-service | 8081 | Login/register, users, roles, permissions |
| user-management | 8082 | User profiles (name, contact, DOB) |
| student-management | 8083 | Students, student enrollments |
| academic-management | 8084 | Academic years, classes, sections, subjects, class sections |
| faculty-management | 8085 | Teachers, teacher assignments |
| attendance-management | 8087 | Daily per-subject attendance |
| examination-management | 8088 | Exams, exam schedules, exam results |
| platform-core | 8089 | Generic entity/field definitions + records — the domain-agnostic layer |
| `frontend/` | 3000 | React admin UI covering every service above |

Every service shares one MySQL database (`academic_x`), talks to the others over Feign through Eureka for cross-service validation (e.g. an Exam Result checks that its student enrollment and subject actually exist before saving), and returns a consistent `{success, message, data}` response wrapper (with some inconsistencies — see `frontend/README.md`).

**Not built yet** (seeded as roles/aspirations, no backend service exists): Fee/Finance, Library, HR, Notifications, Reporting/Analytics. Registering an ERP role like Accountant or Librarian today gets you a login with nowhere to go — the frontend says so plainly rather than faking a dashboard for them.

## Tech stack

- Java 21, Spring Boot 3.5, Spring Cloud (Eureka, OpenFeign, Gateway)
- Spring Security + JWT — **only in `auth-service`**; the other six business services don't validate a token themselves (see Security model below)
- MySQL (one shared schema, `ddl-auto: update`)
- React 18 + TypeScript + Vite (frontend)

## Running it

1. Start MySQL (schema `academic_x` auto-creates on first connect).
2. Start `eureka-server` (8761).
3. Start `api-gateway` (8080) and the six business services — order doesn't matter beyond Eureka being up first; each registers on boot.
4. `cd frontend && npm install && npm run dev` — runs at `localhost:3000`.
5. Open the frontend, register an account (or log in as the seeded default admin: `admin@academiax.local` / `Admin@123`), and use it like a normal admin tool.

## Security model — read before assuming this is production-ready

- `auth-service` has real Spring Security + JWT (BCrypt passwords, access/refresh tokens, role-based `@PreAuthorize` on its own admin endpoints).
- `api-gateway` enforces a JWT filter on every route except `/api/v1/auth/**` — but two of its route predicates (`academic-management`, `examination-management`) don't actually match those services' real endpoint paths, so those two are unreachable through the gateway as configured today.
- **None of the six business services validate a JWT themselves.** A request that reaches `student-management`, `academic-management`, etc. directly (bypassing the gateway) needs no `Authorization` header at all. The frontend takes advantage of this deliberately — see `frontend/README.md` for why it calls every service directly rather than through the gateway.
- The frontend's role-based navigation (a Teacher sees different pages than a Student) is a **UI-only** convenience layered on top of this — it decides what to show, not what the backend allows. See `frontend/DESIGN.md` for the full role → access model.

None of this is a hidden bug list to feel bad about — it's the honest current state, written down so the next person (or the next session) doesn't have to rediscover it by hitting a 401 or a silent bypass.

## The platform layer (`platform-core`)

This is Phase 1 of turning AcademiaX into a domain-agnostic platform. It's purely additive — nothing about the education services above changed. The planned roadmap: Phase 1 (this) — the metadata engine itself; Phase 2 — real multi-tenancy in `auth-service` (tenant-scoped users, JWT `tenantId` claim) and generic RBAC (tenant-defined roles/permissions scoped to entity types); Phase 3 — optionally re-express simple existing entities (Academic Year, Class, Section, Subject) as platform-core entity definitions to prove the Education tenant can run on the generic core for its simple data.

- **`EntityDefinition`** + **`FieldDefinition`** — define a new record type and its fields (`TEXT`/`NUMBER`/`DATE`/`BOOLEAN`/`ENUM`/`REFERENCE`) at runtime, no code.
- **`EntityRecord`** — the actual data, stored as a JSON column keyed by field name. Validated against the field definitions on every write (required fields, type checks, and reference-existence checks — the generic equivalent of the Feign `exists()` calls the other services use).
- **Frontend**: one dynamic page (`EntityRecordsPage`) renders the exact same `CrudPage` component every other page uses, with its field list fetched from the API instead of hardcoded — so a brand-new entity type gets a full list/create/edit/delete UI with zero new frontend code. Manage entity/field definitions themselves from the "Entity Definitions" admin page.
- **Tenancy today**: a single fixed demo tenant ID sent as `X-Tenant-Id` on every platform-core request (`frontend/src/api/platformApi.ts`) — real multi-tenant onboarding and JWT-carried tenant claims are Phase 2, not built yet.
- **What's still hand-coded on purpose**: grading formulas, double-booking conflict checks, bulk attendance marking — logic too complex to express as field definitions stays as real code in its own service (the "hybrid" architecture decision). `platform-core` doesn't replace `examination-management`/`attendance-management`; it's a parallel capability for everything that's simple CRUD over configurable fields.

## Project structure

```
AcademiaX/
├── eureka-server
├── api-gateway
├── common/                  shared DTOs (ApiResponse), BaseEntity, shared enums
├── auth-service
├── user-management
├── student-management
├── academic-management
├── faculty-management
├── attendance-management
├── examination-management
├── platform-core/           generic entity/field definitions + records (any domain)
└── frontend/                React admin UI — see frontend/README.md and frontend/DESIGN.md
```

## Where to look next

- **`frontend/README.md`** — how to run the frontend, the backend inconsistencies it works around, ports, project structure.
- **`frontend/DESIGN.md`** — the UI/UX design: visual system, navigation, role-based access model, and the interaction patterns (generic CRUD pattern, reference dropdowns, bulk attendance marking) used across it.

## Future Enhancements

- Parent Portal
- AI-based Attendance
- Face Recognition
- Timetable Generator
- Mobile Application
- Online Examination
- Fee/Finance Management
- Library Management
- HR Management
- Notification Service
- Reporting & Analytics
