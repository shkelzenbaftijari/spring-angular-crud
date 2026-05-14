# Spring Angular CRUD

> Full-stack CRUD demo — Spring Boot 4 REST API + Angular 21 frontend.
> Demonstrates layered architecture, JWT authentication, pagination, and responsive UI.

---

## Why this project?

A clean, end-to-end reference for common enterprise patterns that appear in real Java/Angular projects:
structured layers, DTO-based API contracts, stateless JWT security, offset pagination, and a mobile-first UI.

No shortcuts — the full stack runs locally from two commands.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 25, Spring Boot 4, Spring Security, Spring Data JPA |
| Database | H2 (in-memory file, dev mode) — swap with PostgreSQL in production |
| Auth | JWT (JJWT 0.12.6), BCrypt, jHipster-style `DomainUserDetailsService` |
| Frontend | Angular 21, PrimeNG 21 (Aura/Teal theme), Tailwind CSS v4 |
| Package manager | pnpm |

---

## Patterns Demonstrated

| Pattern | Where |
|---|---|
| Layered architecture | Entity → Repository → Service → Controller |
| DTO pattern | `ClientDTO` as API contract, `ClientMapper` for conversion |
| Global exception handling | `GlobalExceptionHandler` (`@RestControllerAdvice`) |
| Validation | Bean Validation on entity + DTO (`@NotBlank`, `@Email`, `@Size`) |
| Offset pagination | `PageResult<T>` record, `PageRequest` + `Sort` |
| Stateless JWT auth | `JwtTokenProvider`, `JwtFilter`, `AuthInterceptor` |
| Role-based access | `ROLE_USER` / `ROLE_ADMIN` via `Authority` entity |
| SLF4J logging | Warn on not-found, info on create/update/delete |
| Angular Signals | `signal()`, `computed()` — reactive state without RxJS Subject overhead |
| Inline editing | Cell-level edit with automatic revert on API error |

---

## Project Structure

```
spring-angular-crud/
├── backend/
│   └── src/main/java/com/invoicehub/
│       ├── config/         SecurityConfiguration, Constants
│       ├── controller/     ClientController, AuthController, UserController
│       ├── dto/            ClientDTO, PageResult, LoginRequest, JwtResponse, UserDTO
│       ├── entity/         Client, User, Authority
│       ├── exception/      GlobalExceptionHandler, ResourceNotFoundException, ErrorResponse
│       ├── mapper/         ClientMapper
│       ├── repository/     ClientRepository, UserRepository, AuthorityRepository
│       ├── security/       JwtTokenProvider, JwtFilter, DomainUserDetailsService
│       └── service/        ClientService, UserService
│
└── frontend/
    └── src/app/
        ├── components/
        │   ├── client-list/    Table, inline edit, delete, pagination
        │   ├── client-form/    Create / full edit page
        │   ├── shell/          Sakai-style layout (topbar + sidebar)
        │   ├── login/          Auth form
        │   └── user-management/ Admin user CRUD
        ├── guards/             authGuard (route protection)
        ├── interceptors/       AuthInterceptor (injects JWT header)
        ├── models/             client.model.ts, user.model.ts
        └── services/           ClientService, AuthService, UserService
```

---

## REST API

**Base URL:** `http://localhost:8080`

### Client endpoints

| Method | Path | Description | Auth |
|---|---|---|---|
| `GET` | `/clients?page=0&size=10` | Paginated list (newest first) | Required |
| `GET` | `/clients/{id}` | Get by ID | Required |
| `POST` | `/clients` | Create | Required |
| `PUT` | `/clients/{id}` | Update | Required |
| `DELETE` | `/clients/{id}` | Delete (204) | Required |

### Auth endpoint

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/auth/login` | Login → returns JWT token |

### Paginated response format

```json
{
  "data": [
    { "id": 1, "referenceNumber": "CLT-001", "fullName": "Jane Smith", "email": "jane@example.com", "phone": "+49 170 1234567" }
  ],
  "totalPages": 3,
  "currentPage": 0,
  "totalCount": 25
}
```

### Error response format

```json
{ "status": 404, "message": "Client not found: 42", "timestamp": "14.05.2026 10:30" }
```

---

## Security

| Feature | Detail |
|---|---|
| Password hashing | BCrypt |
| Token format | JWT signed with HS512 |
| Token lifetime | 24 hours (configurable) |
| Protected routes | All `/clients/**` and `/api/users/**` require `Authorization: Bearer <token>` |
| Admin-only | `/api/users/**` requires `ROLE_ADMIN` |
| Stateless | No session — `SessionCreationPolicy.STATELESS` |
| CORS | Configured for `localhost:4200` |

---

## Architecture Overview

```
Browser (Angular 21)
  │
  ├── AuthInterceptor → adds JWT to every request
  ├── AuthGuard       → protects /clients, /users routes
  │
  │   HTTP (localhost:4200 → localhost:8080)
  │
Spring Boot (port 8080)
  │
  ├── JwtFilter           → validates token, sets SecurityContext
  ├── ClientController    → @RestController, @RequestMapping("/clients")
  ├── ClientService       → business logic, SLF4J logging
  ├── ClientMapper        → static toEntity() / toDto()
  ├── ClientRepository    → extends JpaRepository<Client, Long>
  └── H2 Database         → file: ./backend/data/springcrudDB
```

---

## Run Locally

### Backend

```bash
cd backend
./mvnw spring-boot:run
# API running at http://localhost:8080
# H2 console at http://localhost:8080/h2-console
```

**Default test user** (created via H2 console or `data.sql`):
- Login: `admin` / Password: `admin123` / Role: `ROLE_ADMIN`

Or register via the User Management page in the app (requires an existing admin login).

### Frontend

```bash
cd frontend
pnpm install
pnpm start
# App running at http://localhost:4200
```

---

## Roadmap

- [ ] PostgreSQL / Supabase production config
- [ ] `data.sql` seed script (auto-creates admin user on startup)
- [ ] Sorting by all columns (backend + frontend)
- [ ] Search filter passed to backend query (server-side search)
