# Spring Angular CRUD

> Full-stack CRUD application — Spring Boot 4 REST API + Angular 21 frontend.  
> Demonstrates enterprise patterns: layered architecture, JWT auth, DTO contracts, pagination, and a responsive PrimeNG UI.

---

## Why this project?

A clean, end-to-end reference for patterns that appear in real Java/Angular projects.
No shortcuts — the full stack runs locally from two commands, with structured layers, validated API contracts, stateless JWT security, and a mobile-first UI.

---

## Tech Stack

| Layer          | Technology                                                          |
|----------------|---------------------------------------------------------------------|
| Backend        | Java 25, Spring Boot 4, Spring Security, Spring Data JPA           |
| Database       | H2 (file-based, dev mode) — swap with PostgreSQL for production    |
| Auth           | JWT (JJWT 0.12.6), BCrypt, jHipster-style `DomainUserDetailsService` |
| Frontend       | Angular 21, PrimeNG 21 (Aura/Teal theme), Tailwind CSS v4          |
| Package manager | pnpm                                                               |

---

## Patterns Implemented

| Pattern                  | Detail                                                                 |
|--------------------------|------------------------------------------------------------------------|
| Layered architecture     | Entity → Repository → Service → Controller                             |
| DTO + manual mapping     | `ClientDTO` as API contract, `ClientMapper` with static methods        |
| Global exception handler | `GlobalExceptionHandler` (`@RestControllerAdvice`) → structured `ErrorResponse` |
| Bean Validation          | `@NotBlank`, `@Email`, `@Size` on entity + DTO; `@Valid` on controllers |
| Offset pagination        | `PageResult<T>` record, `PageRequest` + `Sort` by ID descending        |
| Stateless JWT auth       | `JwtTokenProvider` (HS512), `JwtFilter` per request, `AuthInterceptor` |
| Role-based access        | `ROLE_USER` / `ROLE_ADMIN` via `Authority` entity, enforced in filter chain |
| SLF4J logging            | `warn` on not-found, `info` on create / update / delete                |
| Angular Signals          | `signal()`, `computed()` — reactive state without RxJS Subject overhead |
| Inline editing           | Cell-level edit with automatic revert on API error                     |
| Error interceptor        | Angular HTTP interceptor catches 401 → redirects to login              |
| Auth guard               | `canActivate` protects all routes except `/login`                      |
| Responsive layout        | Sakai-style: floating panels, sidebar (desktop), bottom nav (mobile)   |

---

## Project Structure

```
spring-angular-crud/
├── backend/
│   └── src/main/java/com/springcrud/
│       ├── config/         SecurityConfiguration, Constants
│       ├── controller/     ClientController, AuthController, UserController
│       ├── dto/            ClientDTO, PageResult, LoginRequest, JwtResponse, UserDTO, RegisterRequest
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
        │   ├── client-list/        Table, inline edit, delete, pagination
        │   ├── client-form/        Create / full edit page
        │   ├── shell/              Sakai-style layout (topbar + sidebar)
        │   ├── login/              Auth form
        │   └── user-management/    Admin user CRUD
        ├── guards/             authGuard (route protection)
        ├── interceptors/       AuthInterceptor (JWT), ErrorInterceptor (401 redirect)
        ├── models/             client.model.ts, user.model.ts
        └── services/           ClientService, AuthService, UserService
```

---

## REST API

**Base URL:** `http://localhost:8080`

### Client endpoints

| Method   | Path                        | Description              | Auth     |
|----------|-----------------------------|--------------------------|----------|
| `GET`    | `/clients?page=0&size=10`   | Paginated list (newest first) | Required |
| `GET`    | `/clients/{id}`             | Get by ID                | Required |
| `POST`   | `/clients`                  | Create                   | Required |
| `PUT`    | `/clients/{id}`             | Update                   | Required |
| `DELETE` | `/clients/{id}`             | Delete (204 No Content)  | Required |

### Auth endpoint

| Method | Path               | Description              |
|--------|--------------------|--------------------------|
| `POST` | `/api/auth/login`  | Login → returns JWT token |

### Paginated response

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

### Error response

```json
{ "status": 404, "message": "Client not found: 42", "timestamp": "14.05.2026 10:30" }
```

---

## Security

| Feature          | Detail                                                            |
|------------------|-------------------------------------------------------------------|
| Password hashing | BCrypt                                                            |
| Token format     | JWT signed with HS512                                             |
| Token lifetime   | 24 hours (configurable via `app.security.jwt.expiration-seconds`) |
| Protected routes | All `/clients/**` and `/api/users/**` require `Authorization: Bearer <token>` |
| Admin-only       | `/api/users/**` requires `ROLE_ADMIN`                             |
| Stateless        | No server-side session — `SessionCreationPolicy.STATELESS`        |
| CORS             | Configured for `http://localhost:4200`                            |

---

## Architecture Overview

```
Browser (Angular 21)
  │
  ├── AuthInterceptor  → attaches JWT header to every request
  ├── ErrorInterceptor → catches 401, redirects to /login
  ├── authGuard        → blocks unauthenticated route access
  │
  │   HTTP  localhost:4200 → localhost:8080
  │
Spring Boot (port 8080)
  │
  ├── JwtFilter              → validates token on every request, sets SecurityContext
  ├── SecurityConfiguration  → filter chain, CORS, role rules, stateless session
  ├── AuthController         → POST /api/auth/login → JWT
  ├── ClientController       → GET/POST/PUT/DELETE /clients
  ├── ClientService          → business logic, SLF4J logging
  ├── ClientMapper           → static toEntity() / toDto()
  ├── ClientRepository       → JpaRepository<Client, Long>
  └── H2 Database            → file: ./backend/data/springcrudDB
```

---

## Run Locally

### Backend

```bash
cd backend
./mvnw spring-boot:run
# API:       http://localhost:8080
# H2 console: http://localhost:8080/h2-console
```

**Default test user** — create manually via H2 console or `data.sql`:
- Login: `admin` / Password: `admin123` / Role: `ROLE_ADMIN`

### Frontend

```bash
cd frontend
pnpm install
pnpm start
# App: http://localhost:4200
```

---

## Roadmap

Patterns planned for future iterations, grouped by area.

### Backend

| Feature                   | Description                                                              |
|---------------------------|--------------------------------------------------------------------------|
| Liquibase                 | Versioned DB migrations — replace `spring.jpa.hibernate.ddl-auto`       |
| TSID                      | Modern, sortable ID generation — replaces `IDENTITY` auto-increment      |
| JWT Refresh Token         | Short-lived access token + long-lived refresh token rotation             |
| Auditing                  | `@CreatedDate`, `@LastModifiedDate` via Spring Data `@EnableJpaAuditing` |
| Soft Delete               | `@SQLRestriction("deleted = false")` — records deactivated, not removed  |
| OpenAPI / Swagger         | Auto-generated docs at `/swagger-ui.html` via `springdoc-openapi`        |
| MDC Logging               | `MDC.put("requestId", ...)` — correlate logs across a request lifecycle  |
| Rate Limiting             | Bucket4j or Spring's `RateLimiter` — protect public endpoints            |
| Custom Validators         | `@ConstraintValidator` for business rules (e.g. unique reference number) |
| Testcontainers            | Integration tests against a real PostgreSQL container                    |
| Actuator Health Check     | `/actuator/health` endpoint for container readiness probes               |

### Frontend

| Feature              | Description                                                              |
|----------------------|--------------------------------------------------------------------------|
| Lazy Loading         | Route-level lazy loading — only load feature modules when navigated to   |
| Environment Configs  | `environment.ts` / `environment.prod.ts` for API URL switching           |
| Skeleton Loaders     | Placeholder shimmer while data is fetching — replaces spinner            |
| Server-side Search   | Search term passed as query param to backend, not filtered in-memory     |
| Column Sorting       | Clickable headers trigger sorted `GET /clients?sort=fullName,asc`        |

### DevOps

| Feature              | Description                                                              |
|----------------------|--------------------------------------------------------------------------|
| Docker + Compose     | `Dockerfile` for backend + frontend, `docker-compose.yml` to run both   |
| PostgreSQL config    | Production datasource, connection pool (HikariCP), env-based credentials |
| `.env` management    | Secrets (JWT secret, DB password) via `.env` — never committed           |
| GitHub Actions CI    | Build + test pipeline on every push to `main`                            |
