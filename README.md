# StreamRoom — Cyberpunk Streaming Website

A full-stack streaming website for **PiggyPlaysPH** built with React 19 and Spring Boot 3.2, featuring a full-viewport Twitch player, a content management system, and a cyberpunk aesthetic.

---

## Project Structure

```
streamroom/
├── frontend/                    # React 19 + Vite application
│   ├── src/
│   │   ├── api/
│   │   │   ├── client.js        # Axios instance + response interceptor
│   │   │   └── services/        # Grouped API service modules
│   │   │       ├── contentService.js
│   │   │       ├── gameService.js
│   │   │       └── userService.js
│   │   ├── components/
│   │   │   ├── common/
│   │   │   │   └── ErrorBoundary.jsx
│   │   │   ├── AdminPanel.jsx
│   │   │   ├── ContentCard.jsx
│   │   │   ├── Footer.jsx
│   │   │   ├── GameCard.jsx
│   │   │   ├── Header.jsx
│   │   │   ├── Hero.jsx
│   │   │   ├── Layout.jsx
│   │   │   └── TwitchEmbed.jsx
│   │   ├── hooks/
│   │   │   └── useApi.js        # Generic data-fetching hook
│   │   ├── pages/
│   │   │   ├── Content.jsx
│   │   │   ├── ContentDetail.jsx
│   │   │   ├── Games.jsx
│   │   │   ├── Home.jsx
│   │   │   ├── NotFound.jsx
│   │   │   └── Profile.jsx
│   │   └── styles/
│   ├── public/
│   │   └── logo.png
│   ├── .env                     # Local secrets — never commit
│   ├── .env.example
│   └── vite.config.js
│
├── backend/                     # Spring Boot 3.2 + Java 25
│   └── src/main/java/com/streamroom/
│       ├── config/
│       │   ├── CorsConfig.java          # Centralized CORS (property-driven)
│       │   ├── TwitchProperties.java    # @ConfigurationProperties for Twitch
│       │   └── WebClientConfig.java
│       ├── controller/                  # REST controllers (interface-injected)
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
│       │   ├── SlugGeneratorService.java
│       │   ├── TwitchService.java
│       │   └── UserService.java
│       └── StreamroomApplication.java
│   └── src/main/resources/
│       ├── application.properties       # Shared defaults + active profile
│       ├── application-dev.properties   # Dev secrets — gitignored
│       └── application-prod.properties  # Prod — reads from environment variables
│
├── docker-compose.yml           # PostgreSQL 15 for local dev
└── README.md
```

---

## Pages

| Route | Description |
|---|---|
| `/` | Home — full-viewport Twitch player with live chat |
| `/games` | Game collection |
| `/content` | Content library |
| `/content/:slug` | Content detail |
| `/profile` | PiggyPlaysPH profile, schedule, and socials |

---

## Technology Stack

### Frontend
- **React 19** with functional components and hooks
- **React Router v6** — client-side routing with nested layouts
- **Axios** — centralized service modules with a response error interceptor
- **Vite 7** — dev server with `/api` proxy to backend
- **Tailwind CSS 3** + custom CSS with cyberpunk CSS variables
- **React Icons**

### Backend
- **Java 25 / Spring Boot 3.2**
- **Spring Data JPA** + **PostgreSQL**
- **Spring Boot Validation** — Jakarta Bean Validation on all write DTOs
- **Springdoc OpenAPI** — Swagger UI at `/api/swagger-ui.html` (dev only)
- **Lombok**
- **Spring WebFlux** — WebClient for Twitch API
- **JJWT** — JWT library ready for auth implementation

---

## API Reference

| Method | Path | Description |
|---|---|---|
| GET | `/api/health` | Health check |
| GET | `/api/content` | All published content |
| GET | `/api/content/{id}` | Content by ID |
| GET | `/api/content/slug/{slug}` | Content by slug |
| GET | `/api/content/featured` | Featured content |
| GET | `/api/content/author/{id}` | Content by author |
| POST | `/api/content` | Create content (`X-User-Id` header required) |
| PUT | `/api/content/{id}` | Update content |
| DELETE | `/api/content/{id}` | Delete content |
| GET | `/api/games` | All games |
| GET | `/api/games/{id}` | Game by ID |
| GET | `/api/games/featured` | Featured games |
| POST | `/api/games` | Create game |
| PUT | `/api/games/{id}` | Update game |
| DELETE | `/api/games/{id}` | Delete game |
| GET | `/api/users` | All users |
| GET | `/api/users/{id}` | User by ID |
| GET | `/api/users/username/{username}` | User by username |
| POST | `/api/users` | Create user |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |

Interactive docs at `http://localhost:8080/api/swagger-ui.html` when running the `dev` profile.

---

## Architecture Highlights

The backend follows **SOLID principles**:

| Principle | Implementation |
|---|---|
| **SRP** | `DtoMapper` handles all entity→DTO conversion; `SlugGeneratorService` owns slug logic; `CorsConfig` owns CORS config |
| **OCP** | Services are extensible via interfaces; CORS origins are property-driven, not hardcoded |
| **LSP** | Controllers depend on `IContentService`, `IGameService`, `IUserService` — swap implementations without changing callers |
| **ISP** | Separate per-domain service interfaces; no fat shared contracts |
| **DIP** | `TwitchProperties` via `@ConfigurationProperties`; controllers inject interfaces, not concrete classes |

Additional backend patterns:
- All write operations use `@Transactional`; reads use `@Transactional(readOnly = true)`
- All errors return a consistent `ErrorResponse` JSON envelope (`status`, `error`, `message`, `timestamp`, `details`) via `GlobalExceptionHandler`
- Profile-based config: `dev` uses local values, `prod` reads from environment variables — no secrets in source control

---

## Future Enhancements

- [ ] JWT authentication and authorization
- [ ] Admin dashboard
- [ ] Real Twitch Helix API integration
- [ ] Image and video upload
- [ ] Stream analytics
- [ ] Comments system
- [ ] Search and filtering
- [ ] Database migrations with Flyway

---

## License

MIT
