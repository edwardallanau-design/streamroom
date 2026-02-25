# StreamRoom — Cyberpunk Streaming Website

A full-stack streaming website for **PiggyPlaysPH** built with React 19 and Spring Boot 4.0.3, featuring a full-viewport Twitch player, a blog/content management system, JWT-based admin authentication, and a cyberpunk aesthetic.

**All services are fully containerized with Docker** — run the entire application stack with one command!

---

## Project Structure

```
streamroom/
├── frontend/                    # React 19 + Vite application
│   ├── src/
│   │   ├── api/
│   │   │   ├── client.js        # Axios instance + response interceptor
│   │   │   └── services/        # Grouped API service modules
│   │   │       ├── contentService.js   # Public + admin CRUD methods
│   │   │       ├── gameService.js
│   │   │       └── userService.js
│   │   ├── components/
│   │   │   ├── common/
│   │   │   │   └── ErrorBoundary.jsx
│   │   │   ├── AdminPanel.jsx
│   │   │   ├── ContentCard.jsx  # Horizontal card with admin actions + delete modal
│   │   │   ├── Footer.jsx       # Fixed to bottom of viewport
│   │   │   ├── GameCard.jsx
│   │   │   ├── Header.jsx
│   │   │   ├── Hero.jsx         # Full-viewport Twitch embed
│   │   │   ├── Layout.jsx
│   │   │   └── TwitchEmbed.jsx
│   │   ├── contexts/
│   │   │   └── AuthContext.jsx  # JWT auth state + isAdmin flag
│   │   ├── hooks/
│   │   │   └── useApi.js        # Generic data-fetching hook
│   │   ├── pages/
│   │   │   ├── AdminLogin.jsx   # Admin-only login form
│   │   │   ├── Content.jsx      # Content library (admin: all posts; guest: published only)
│   │   │   ├── ContentDetail.jsx # Full post with HTML body render + admin edit/delete
│   │   │   ├── ContentForm.jsx  # Create / edit post form with media toolbar
│   │   │   ├── Games.jsx
│   │   │   ├── Home.jsx
│   │   │   ├── NotFound.jsx
│   │   │   └── Profile.jsx      # Editable admin profile with schedule & socials
│   │   └── styles/
│   ├── public/
│   │   └── logo.png
│   ├── .env                     # Local secrets — never commit
│   ├── .env.example
│   └── vite.config.js           # /api proxy to backend + HMR polling for Docker
│
├── backend/                     # Spring Boot 4.0.3 + Java 25
│   └── src/main/java/com/streamroom/
│       ├── config/
│       │   ├── AdminInterceptor.java    # JWT validation for all /admin/** routes
│       │   ├── CorsConfig.java          # Centralized CORS (property-driven)
│       │   ├── TwitchProperties.java    # @ConfigurationProperties for Twitch
│       │   └── WebClientConfig.java
│       ├── controller/
│       │   ├── AdminContentController.java  # Admin CRUD at /admin/content
│       │   ├── AuthController.java          # POST /auth/login → JWT
│       │   ├── ContentController.java       # Public read endpoints
│       │   ├── GameController.java
│       │   ├── HealthController.java
│       │   ├── ProfileController.java
│       │   └── UserController.java
│       ├── dto/                         # DTOs with Jakarta Validation annotations
│       ├── entity/                      # JPA entities
│       ├── exception/
│       │   ├── ErrorResponse.java       # Consistent JSON error envelope
│       │   ├── GlobalExceptionHandler.java
│       │   └── ResourceNotFoundException.java
│       ├── mapper/
│       │   └── DtoMapper.java           # Central entity-to-DTO mapping
│       ├── repository/
│       ├── service/
│       │   ├── IContentService.java     # Service interfaces (DIP)
│       │   ├── IGameService.java
│       │   ├── IUserService.java
│       │   ├── ContentService.java
│       │   ├── GameService.java
│       │   ├── JwtService.java          # JWT generation + validation
│       │   ├── SlugGeneratorService.java # Unique slug generation with collision handling
│       │   ├── TwitchService.java
│       │   └── UserService.java
│       └── StreamroomApplication.java
│   └── src/main/resources/
│       ├── application.properties       # Shared defaults + active profile
│       ├── application-dev.properties   # Dev secrets — gitignored
│       └── application-prod.properties  # Prod — reads from environment variables
│
├── .githooks/                   # Git hooks (pre-push build check)
├── docker-compose.yml           # All services: postgres, backend, frontend, frontend-dev
└── README.md
```

---

## Pages

| Route | Description | Auth |
|---|---|---|
| `/` | Home — full-viewport Twitch player with live chat | Public |
| `/games` | Game collection | Public |
| `/content` | Content library — published posts; admin sees all including drafts | Public |
| `/content/:slug` | Post detail — renders HTML body with embedded media | Public |
| `/content/new` | Create new post with media toolbar | Admin only |
| `/content/edit/:id` | Edit existing post | Admin only |
| `/profile` | PiggyPlaysPH profile, schedule, and socials (editable by admin) | Public |
| `/admin` | Admin login | Public |

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

## Admin Access

1. Navigate to `/admin` and log in with the admin account credentials.
2. A JWT token is stored in `localStorage` and sent as `Authorization: Bearer <token>` on all subsequent requests.
3. All `/admin/**` routes on the backend are protected by `AdminInterceptor` — requests without a valid JWT receive `401 Unauthorized`.

### Admin Capabilities

| Feature | How |
|---|---|
| Create post | `/content` → NEW POST button |
| Edit post | Card → Edit, or post detail → Edit |
| Delete post | Card or post detail → Delete → confirm modal |
| Publish / unpublish | Toggle in the post form |
| Edit profile | Profile page → Edit Profile |
| View drafts | Content library shows all posts including unpublished |

### Content Body Editor

The post form includes a toolbar that inserts HTML snippets at the cursor:

| Button | Inserts |
|---|---|
| H2 | `<h2>Heading</h2>` |
| B | Wraps selection in `<strong>` |
| IMG | Prompts for URL → `<img>` tag |
| YT | Prompts for YouTube URL → responsive `<iframe>` embed |
| TTV | Prompts for Twitch clip URL → responsive `<iframe>` embed |

The body is stored as HTML and rendered with `dangerouslySetInnerHTML` on the detail page.

---

## API Reference

### Public

| Method | Path | Description |
|---|---|---|
| GET | `/api/health` | Health check |
| POST | `/api/auth/login` | Admin login → JWT |
| GET | `/api/content` | All published content (newest first) |
| GET | `/api/content/{id}` | Content by ID |
| GET | `/api/content/slug/{slug}` | Content by slug |
| GET | `/api/content/featured` | Featured content |
| GET | `/api/content/author/{id}` | Content by author |
| GET | `/api/games` | All games |
| GET | `/api/games/{id}` | Game by ID |
| GET | `/api/games/featured` | Featured games |
| GET | `/api/users/{id}` | User by ID |
| GET | `/api/users/username/{username}` | User by username |

### Admin (JWT required — `Authorization: Bearer <token>`)

| Method | Path | Description |
|---|---|---|
| GET | `/api/admin/content` | All content including drafts (newest first) |
| POST | `/api/admin/content` | Create post |
| PUT | `/api/admin/content/{id}` | Update post |
| DELETE | `/api/admin/content/{id}` | Delete post |
| GET | `/api/admin/profile` | Admin profile |
| PUT | `/api/admin/profile` | Update admin profile |

Interactive docs:
- **Via Docker (recommended):** `http://localhost/swagger-ui/index.html`
- **Direct backend (local dev):** `http://localhost:8080/swagger-ui/index.html`

---

## Architecture Highlights

The backend follows **SOLID principles**:

| Principle | Implementation |
|---|---|
| **SRP** | `DtoMapper` handles all entity→DTO conversion; `SlugGeneratorService` owns unique slug logic; `JwtService` owns token generation/validation |
| **OCP** | Services are extensible via interfaces; CORS origins are property-driven, not hardcoded |
| **LSP** | Controllers depend on `IContentService`, `IGameService`, `IUserService` — swap implementations without changing callers |
| **ISP** | Separate per-domain service interfaces; no fat shared contracts |
| **DIP** | `TwitchProperties` via `@ConfigurationProperties`; controllers inject interfaces, not concrete classes |

Additional backend patterns:
- All write operations use `@Transactional`; reads use `@Transactional(readOnly = true)`
- `@PrePersist` defaults (`isPublished = false`, `isFeatured = false`) only apply when the field is `null`, so values set before save are preserved
- All errors return a consistent `ErrorResponse` JSON envelope (`status`, `error`, `message`, `timestamp`, `details`) via `GlobalExceptionHandler`
- Profile-based config: `dev` uses local values, `prod` reads from environment variables — no secrets in source control
- Slug uniqueness enforced at the application layer: `SlugGeneratorService` queries the DB and appends `-2`, `-3`, … on collision

---

## Future Enhancements

- [ ] Image and video upload (S3 / object storage)
- [ ] Comments system
- [ ] Search and filtering
- [ ] Database migrations with Flyway
- [ ] Real Twitch Helix API integration (live viewer count, stream status)
- [ ] Stream analytics dashboard
