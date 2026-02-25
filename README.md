# StreamRoom — Cyberpunk Streaming Website

A full-stack streaming website for **PiggyPlaysPH** built with React 19 and Spring Boot 4.0, featuring a full-viewport Twitch player, a role-based content management system, JWT authentication, and a cyberpunk aesthetic.

**All services are fully containerized with Docker** — run the entire application stack with one command!

---

## Project Structure

```
streamroom/
├── frontend/                    # React 19 + Vite application
│   └── src/
│       ├── api/
│       │   ├── client.js                    # Axios instance + JWT interceptor
│       │   └── services/
│       │       ├── contentService.js        # Public + admin CRUD + publishAdmin
│       │       ├── userManagementService.js # Admin user CRUD
│       │       ├── gameService.js
│       │       └── profileService.js
│       ├── components/
│       │   ├── ContentCard.jsx  # Card with Edit / Publish / Delete + confirm modals
│       │   ├── Header.jsx       # DASHBOARD link (any role); USERS link (canManageUsers)
│       │   ├── Hero.jsx         # Full-viewport Twitch embed
│       │   ├── Footer.jsx
│       │   └── ...
│       ├── contexts/
│       │   └── AuthContext.jsx  # Role state + computed permission flags
│       ├── hooks/
│       │   └── useApi.js
│       ├── pages/
│       │   ├── AdminLogin.jsx    # Login form / role-aware dashboard
│       │   ├── Content.jsx       # Content library
│       │   ├── ContentDetail.jsx # Post detail + Publish button
│       │   ├── ContentForm.jsx   # Create / edit post (no publish checkbox)
│       │   ├── UserManagement.jsx # Admin user management
│       │   ├── Games.jsx
│       │   ├── Home.jsx
│       │   ├── Profile.jsx
│       │   └── NotFound.jsx
│       └── styles/
│
├── backend/                     # Spring Boot 4.0 + Java 25
│   └── src/main/java/com/streamroom/
│       ├── config/
│       │   ├── AdminInitializer.java    # Creates default ADMIN on startup; migrates legacy users
│       │   ├── AdminInterceptor.java    # JWT validation; extracts userId + role into request attrs
│       │   ├── CorsConfig.java
│       │   └── WebClientConfig.java
│       ├── controller/
│       │   ├── AdminContentController.java  # Role-aware content CRUD + publish endpoint
│       │   ├── AdminUserController.java     # User management (ADMIN/MOD only)
│       │   ├── AuthController.java          # POST /admin/login → JWT + role
│       │   ├── ContentController.java       # Public read endpoints
│       │   ├── GameController.java
│       │   ├── HealthController.java
│       │   └── ProfileController.java       # Profile + password change
│       ├── dto/
│       │   ├── ContentDTO.java              # Timestamps as UTC ISO-8601 strings
│       │   ├── LoginResponse.java           # token, userId, username, displayName, role
│       │   ├── AdminUserDTO.java
│       │   ├── CreateUserAdminRequest.java
│       │   ├── UpdateRoleRequest.java
│       │   ├── ChangePasswordRequest.java
│       │   └── ...
│       ├── entity/
│       │   ├── Content.java   # createdAt/updatedAt/publishedAt use Instant (UTC)
│       │   ├── User.java      # role field (Role enum)
│       │   └── ...
│       ├── enums/
│       │   └── Role.java      # ADMIN, MODERATOR, CONTENT_CREATOR
│       ├── exception/
│       │   ├── ForbiddenException.java         # → 403
│       │   ├── ResourceNotFoundException.java  # → 404
│       │   └── GlobalExceptionHandler.java
│       ├── mapper/
│       │   └── DtoMapper.java   # Instant.toString() → "2024-01-26T14:30:00Z"
│       ├── repository/
│       └── service/
│           ├── AdminUserService.java   # Permission-enforced user CRUD
│           ├── ContentService.java
│           ├── JwtService.java         # Claims: userId + role
│           ├── ProfileService.java     # BCrypt password change
│           ├── SlugGeneratorService.java
│           └── ...
│
├── docker-compose.yml
└── README.md
```

---

## Roles & Permissions

Three roles control access throughout the application:

| Action | ADMIN | MODERATOR | CONTENT_CREATOR |
|---|:---:|:---:|:---:|
| Login via `/admin` | ✓ | ✓ | ✓ |
| View all posts (incl. drafts) | ✓ | ✓ | ✓ |
| Create post | ✓ | ✓ | ✓ |
| Edit any post | ✓ | ✓ | ✗ |
| Edit own post | ✓ | ✓ | ✓ |
| Delete any post | ✓ | ✓ | ✗ |
| Delete own post | ✓ | ✓ | ✓ |
| Publish / Unpublish | ✓ | ✓ | ✗ |
| Edit profile | ✓ | ✓ | ✗ |
| Change own password | ✓ | ✓ | ✓ |
| Manage users | ✓ | ✓ (MOD+CC only) | ✗ |

The default **ADMIN** account is created automatically on first startup using `admin.username` / `admin.password` from `application.properties`.

---

## Pages

| Route | Description | Access |
|---|---|---|
| `/` | Home — full-viewport Twitch player | Public |
| `/games` | Game collection | Public |
| `/content` | Content library | Public (guests see published only) |
| `/content/:slug` | Post detail with Publish/Edit/Delete actions | Public |
| `/content/new` | Create post with media toolbar | Any role |
| `/content/edit/:id` | Edit post | Any role (CC: own only) |
| `/profile` | Streamer profile, schedule, socials | Public |
| `/admin` | Login form / role-aware dashboard | Public |
| `/admin/users` | User management | ADMIN, MODERATOR |

---

## Running Locally

### Production (single command)

```bash
docker compose up --build
```

Starts postgres + backend + Nginx-served frontend at **http://localhost**.

### Development with HMR

```bash
docker compose up --build frontend-dev backend postgres
```

Starts postgres + backend + Vite dev server with hot module replacement at **http://localhost:5173**.

File changes under `frontend/src/` are reflected instantly without rebuilding.

> **VSCode:** Use the **Docker: Dev (HMR)** task (`Ctrl+Shift+P` → Run Task).

### Environment Variables

Copy `.env.example` to `.env` in `frontend/`:

```env
VITE_API_BASE_URL=/api
VITE_TWITCH_CHANNEL=piggyplaysph
```

`VITE_API_BASE_URL` must be a relative path (`/api`) so Vite's proxy rewrites requests to the backend correctly.

---

## Admin Dashboard

Navigate to `/admin` and log in. When authenticated, `/admin` becomes a dashboard showing:

- Logged-in username and role badge
- Navigation links: Home, Games, Content Library, User Management *(ADMIN/MOD only)*, Profile
- **Change Password** button — requires current password verification
- **Logout** button

A JWT is stored in `localStorage` and sent as `Authorization: Bearer <token>` on all subsequent requests. All `/admin/**` backend routes are protected by `AdminInterceptor` — requests without a valid token receive `401 Unauthorized`.

---

## Content Publishing Workflow

1. Any logged-in role creates a post via **+ NEW POST** — new posts are saved as drafts.
2. ADMIN and MODERATOR can publish a draft using the **Publish** button on the content card or inside the post detail. A confirmation modal is shown before the state changes.
3. Published posts appear to all visitors. Unpublishing hides them from the public again.
4. CONTENT_CREATOR users see all posts (including drafts from all roles) but cannot publish.

### Post Body Editor

The post form includes a toolbar that inserts HTML snippets at the cursor:

| Button | Inserts |
|---|---|
| H2 | `<h2>Heading</h2>` |
| B | Wraps selection in `<strong>` |
| IMG | Prompts for URL → `<img>` tag |
| YT | Prompts for YouTube URL → responsive `<iframe>` embed |
| TTV | Prompts for Twitch clip URL → responsive `<iframe>` embed |

---

## API Reference

### Public

| Method | Path | Description |
|---|---|---|
| GET | `/api/health` | Health check |
| GET | `/api/content` | All published content (newest first) |
| GET | `/api/content/slug/{slug}` | Content by slug |
| GET | `/api/content/featured` | Featured content |
| GET | `/api/games` | All games |
| GET | `/api/games/featured` | Featured games |
| GET | `/api/profile` | Public streamer profile |

### Admin (JWT required)

| Method | Path | Access | Description |
|---|---|---|---|
| POST | `/api/admin/login` | Public | Login → JWT + role |
| GET | `/api/admin/content` | All roles | All posts incl. drafts |
| POST | `/api/admin/content` | All roles | Create post |
| PUT | `/api/admin/content/{id}` | All roles | Update post (CC: own only) |
| PATCH | `/api/admin/content/{id}/publish` | ADMIN, MOD | Toggle published |
| DELETE | `/api/admin/content/{id}` | All roles | Delete post (CC: own only) |
| GET | `/api/admin/users` | ADMIN, MOD | List users |
| POST | `/api/admin/users` | ADMIN, MOD | Create user |
| PUT | `/api/admin/users/{id}/role` | ADMIN, MOD | Change role |
| DELETE | `/api/admin/users/{id}` | ADMIN, MOD | Delete user |
| GET | `/api/profile` | Public | Streamer profile |
| PUT | `/api/admin/profile` | ADMIN, MOD | Update profile |
| POST | `/api/admin/profile/password` | All roles | Change own password |

Interactive docs: `http://localhost/swagger-ui/index.html` (Docker) or `http://localhost:8080/swagger-ui/index.html` (local dev)

---

## Architecture Highlights

**Backend (SOLID principles):**

| Principle | Implementation |
|---|---|
| **SRP** | `DtoMapper` handles all entity→DTO conversion; `SlugGeneratorService` owns slug logic; `JwtService` owns token generation/validation |
| **OCP** | Services extensible via interfaces; CORS origins are property-driven |
| **LSP** | Controllers depend on `IContentService`, `IAdminUserService`, etc. |
| **ISP** | Separate per-domain service interfaces |
| **DIP** | `TwitchProperties` via `@ConfigurationProperties`; controllers inject interfaces |

**Additional patterns:**
- All write operations use `@Transactional`; reads use `@Transactional(readOnly = true)`
- JWT claims include `userId` and `role`; `AdminInterceptor` stores both as request attributes
- `ForbiddenException` (403) enforced at the service layer for role/ownership violations
- `Instant` (UTC) used for all `Content` timestamps; serialized via `.toString()` to guarantee `Z` suffix
- Profile-based config: `dev` uses local values, `prod` reads from environment variables

---

## Future Enhancements

- [ ] Image and video upload (S3 / object storage)
- [ ] Comments system
- [ ] Search and filtering
- [ ] Database migrations with Flyway
- [ ] Real Twitch Helix API integration (live viewer count, stream status)
- [ ] Stream analytics dashboard
