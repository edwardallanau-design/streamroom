# StreamRoom Backend

A Spring Boot 4.0 / Java 25 backend for a Twitch streaming website with role-based content management.

## Features

- RESTful API for content management
- Role-based access control (ADMIN, MODERATOR, CONTENT_CREATOR)
- User management with tiered permission enforcement
- Twitch integration
- Game library management
- Stream session tracking
- PostgreSQL database support

## Prerequisites

- Java 25
- Maven 3.9+
- PostgreSQL 13+

## Installation

### 1. Clone the repository

```bash
cd backend
```

### 2. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE streamroom;
CREATE USER streamroom_user WITH PASSWORD 'streamroom_password';
ALTER ROLE streamroom_user SET client_encoding TO 'utf8';
ALTER ROLE streamroom_user SET default_transaction_isolation TO 'read committed';
ALTER ROLE streamroom_user SET default_transaction_deferrable TO on;
ALTER ROLE streamroom_user SET timezone TO 'UTC';
GRANT ALL PRIVILEGES ON DATABASE streamroom TO streamroom_user;
```

### 3. Configuration

Update `src/main/resources/application.properties` with your values:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/streamroom
spring.datasource.username=streamroom_user
spring.datasource.password=streamroom_password

jwt.secret=your_super_secret_key_change_this_in_production
jwt.expiration=86400000

admin.username=your_admin_username
admin.password=your_admin_password

twitch.api.client-id=your_client_id
twitch.api.access-token=your_access_token
```

### 4. Build

```bash
mvn clean install
```

### 5. Run

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080/api`.
Swagger UI: `http://localhost:8080/swagger-ui/index.html`

---

## Roles & Permissions

| Action | ADMIN | MODERATOR | CONTENT_CREATOR |
|---|:---:|:---:|:---:|
| Login via /admin | ✓ | ✓ | ✓ |
| Edit own profile | ✓ | ✓ | ✗ |
| Create post | ✓ | ✓ | ✓ |
| Edit any post | ✓ | ✓ | ✗ |
| Edit own post | ✓ | ✓ | ✓ |
| Delete any post | ✓ | ✓ | ✗ |
| Delete own post | ✓ | ✓ | ✓ |
| Publish / Unpublish | ✓ | ✓ | ✗ |
| View all posts (incl. drafts) | ✓ | ✓ | ✓ |
| Manage users | ✓ | ✓ (MOD+CC only) | ✗ |

**Default admin** is created on first startup using `admin.username` / `admin.password` from properties.

---

## API Endpoints

### Auth

| Method | Path | Access | Description |
|---|---|---|---|
| POST | `/api/admin/login` | Public | Login; returns JWT + role |

### Public Content

| Method | Path | Description |
|---|---|---|
| GET | `/api/content` | All published posts |
| GET | `/api/content/slug/{slug}` | Post by slug |
| GET | `/api/content/featured` | Featured posts |

### Admin Content (`/api/admin/content`)

All require valid JWT.

| Method | Path | Access | Description |
|---|---|---|---|
| GET | `/api/admin/content` | All roles | All posts (role-filtered on edit/delete) |
| POST | `/api/admin/content` | All roles | Create post (CC cannot set published) |
| PUT | `/api/admin/content/{id}` | All roles | Update post (CC: own only) |
| PATCH | `/api/admin/content/{id}/publish` | ADMIN, MOD | Toggle published state |
| DELETE | `/api/admin/content/{id}` | All roles | Delete post (CC: own only) |

### User Management (`/api/admin/users`)

Requires ADMIN or MODERATOR role.

| Method | Path | Access | Description |
|---|---|---|---|
| GET | `/api/admin/users` | ADMIN, MOD | List users |
| POST | `/api/admin/users` | ADMIN, MOD | Create user |
| PUT | `/api/admin/users/{id}/role` | ADMIN, MOD | Change user role |
| DELETE | `/api/admin/users/{id}` | ADMIN, MOD | Delete user |

**Permission rules for user management:**
- ADMIN can create/delete/change role of MODERATOR and CONTENT_CREATOR users
- MODERATOR can create/delete/change role of CONTENT_CREATOR users only
- Neither can modify an ADMIN account or their own account

### Profile (`/api/admin/profile`)

| Method | Path | Access | Description |
|---|---|---|---|
| GET | `/api/profile` | Public | Get public profile |
| PUT | `/api/admin/profile` | ADMIN, MOD | Update profile |
| POST | `/api/admin/profile/password` | All roles | Change own password |

### Games

| Method | Path | Description |
|---|---|---|
| GET | `/api/games` | All games |
| GET | `/api/games/{id}` | Game by ID |
| GET | `/api/games/featured` | Featured games |
| POST | `/api/admin/games` | Create game |
| PUT | `/api/admin/games/{id}` | Update game |
| DELETE | `/api/admin/games/{id}` | Delete game |

---

## Project Structure

```
src/
├── main/
│   ├── java/com/streamroom/
│   │   ├── config/
│   │   │   ├── AdminInitializer.java   # Creates default ADMIN on startup; migrates legacy users
│   │   │   ├── AdminInterceptor.java   # JWT validation; extracts userId + role into request attrs
│   │   │   ├── CorsConfig.java
│   │   │   └── WebMvcConfig.java
│   │   ├── controller/
│   │   │   ├── AuthController.java         # POST /admin/login
│   │   │   ├── AdminContentController.java # Role-aware content CRUD
│   │   │   ├── AdminUserController.java    # User management endpoints
│   │   │   ├── ProfileController.java      # Profile + password change
│   │   │   └── ...
│   │   ├── dto/
│   │   │   ├── LoginResponse.java          # token, userId, username, displayName, role
│   │   │   ├── ContentDTO.java             # Timestamps as UTC ISO-8601 strings
│   │   │   ├── AdminUserDTO.java
│   │   │   ├── CreateUserAdminRequest.java
│   │   │   ├── UpdateRoleRequest.java
│   │   │   ├── ChangePasswordRequest.java
│   │   │   └── ...
│   │   ├── entity/
│   │   │   ├── Content.java   # createdAt/updatedAt/publishedAt use Instant (UTC)
│   │   │   ├── User.java      # role field (Role enum, STRING column)
│   │   │   └── ...
│   │   ├── enums/
│   │   │   └── Role.java      # ADMIN, MODERATOR, CONTENT_CREATOR
│   │   ├── exception/
│   │   │   ├── ForbiddenException.java        # 403
│   │   │   ├── ResourceNotFoundException.java  # 404
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── mapper/
│   │   │   └── DtoMapper.java  # Instant.toString() for timestamps → "2024-01-26T14:30:00Z"
│   │   ├── repository/
│   │   ├── service/
│   │   │   ├── AdminUserService.java   # Permission-enforced user CRUD
│   │   │   ├── ContentService.java
│   │   │   ├── JwtService.java         # Claims: userId + role
│   │   │   ├── ProfileService.java     # BCrypt password change
│   │   │   └── ...
│   │   └── StreamroomApplication.java
│   └── resources/
│       └── application.properties
└── test/
```

---

## Technologies

- Spring Boot 4.0 / Java 25
- Spring Data JPA + PostgreSQL
- Spring WebFlux (WebClient for Twitch API)
- Jakarta Bean Validation
- Springdoc OpenAPI 3 (Swagger UI)
- JWT (io.jsonwebtoken 0.12.3) — claims include `userId` and `role`
- Java Records for all DTOs (no Lombok)
- Jackson 3 (`tools.jackson`) — null exclusion via global property
- Virtual threads (Project Loom, `spring.threads.virtual.enabled=true`)
- BCryptPasswordEncoder for password hashing

## License

MIT
