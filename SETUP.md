# StreamRoom — Setup Guide

## Quick Start (Docker - Recommended)

The entire application stack is containerized and ready to run:

```bash
# Build and start all services (frontend, backend, database)
docker-compose up --build

# Application is available at:
# Frontend: http://localhost
# Backend API: http://localhost/api
# Database: localhost:5432
```

That's it! All three services will start automatically with proper networking and health checks.

To stop:

```bash
docker-compose down
```

For complete Docker deployment information, see [DOCKER.md](DOCKER.md).

---

## Prerequisites

- **Docker & Docker Compose** (recommended)
- **Node.js** 18+ and npm (for local frontend development)
- **Java 25** and Maven 3.9+ (for local backend development)
- **PostgreSQL 15+** (only if not using Docker)

---

## Step 1 — Database (Local Development Only)

If you're running the full Docker stack with `docker-compose up`, **skip this step** — the database starts automatically.

For local development without Docker:

**Option A: Docker Container**

```bash
docker-compose up postgres
```

This starts a PostgreSQL 15 container on port `5432` with database `streamroom`, user `streamroom_user`, password `streamroom_password`.

**Option B: Manual PostgreSQL**

```bash
psql -U postgres -c "CREATE DATABASE streamroom;"
psql -U postgres -c "CREATE USER streamroom_user WITH PASSWORD 'streamroom_password';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE streamroom TO streamroom_user;"
```

---

## Step 2 — Backend (Local Development Only)

If you're running the full Docker stack with `docker-compose up`, **skip this step** — the backend starts automatically and is available at `http://localhost/api`.

For local development:

### 2a. Configure secrets

The backend uses Spring profiles. The `dev` profile reads from `application-dev.properties`, which is gitignored so your credentials are never committed.

Create the file from the provided example:

```bash
cp backend/src/main/resources/application.properties.example \
   backend/src/main/resources/application-dev.properties
```

Then open `application-dev.properties` and fill in your values:

```properties
# Database (defaults work if you started PostgreSQL in Step 1)
spring.datasource.url=jdbc:postgresql://localhost:5432/streamroom
spring.datasource.username=streamroom_user
spring.datasource.password=streamroom_password

# Twitch API — get these from https://dev.twitch.tv/console
twitch.api.client-id=your_twitch_client_id_here
twitch.api.access-token=your_twitch_access_token_here

# JWT — change to a random string of at least 32 characters
jwt.secret=replace_this_with_a_secure_random_secret

# CORS — allow the Vite dev server
cors.allowed-origins=http://localhost:3000,http://localhost:5173
```

### 2b. Build and run

```bash
cd backend
mvn clean install -DskipTests
mvn spring-boot:run
```

The API is available at `http://localhost:8080/api`.
Swagger UI is available at `http://localhost:8080/api/swagger-ui.html`.

**Backend Version:** Spring Boot 4.0.3 with Java 25

---

## Step 3 — Frontend (Local Development Only)

If you're running the full Docker stack with `docker-compose up`, **skip this step** — the frontend serves at `http://localhost` automatically.

For local development:

```bash
cd frontend

# Install dependencies
npm install

# Copy and configure environment
cp .env.example .env
```

Edit `.env`:

```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_TWITCH_CHANNEL=your_channel_name
VITE_TWITCH_CLIENT_ID=your_twitch_client_id
```

Start the dev server:

```bash
npm run dev
```

Frontend is available at `http://localhost:5173`.

---

## Project Layout

```
streamroom/
├── frontend/
│   ├── src/
│   │   ├── api/
│   │   │   ├── client.js              # Axios instance + error interceptor
│   │   │   └── services/              # Per-domain API service modules
│   │   │       ├── contentService.js
│   │   │       ├── gameService.js
│   │   │       └── userService.js
│   │   ├── components/
│   │   │   ├── common/
│   │   │   │   └── ErrorBoundary.jsx  # React error boundary for all routes
│   │   │   └── ...
│   │   ├── hooks/
│   │   │   └── useApi.js              # Generic fetch hook (data/loading/error/refetch)
│   │   ├── pages/                     # Home, Games, Content, Profile, NotFound
│   │   └── styles/                    # Per-component CSS + globals
│   ├── public/
│   │   └── logo.png                   # PiggyPlaysPH mascot logo
│   └── .env                           # Gitignored — local secrets only
│
├── backend/
│   └── src/main/java/com/streamroom/
│       ├── config/
│       │   ├── CorsConfig.java        # CORS — reads cors.allowed-origins property
│       │   └── TwitchProperties.java  # Twitch credentials via @ConfigurationProperties
│       ├── exception/
│       │   ├── GlobalExceptionHandler.java     # @RestControllerAdvice
│       │   ├── ResourceNotFoundException.java
│       │   └── ErrorResponse.java              # Consistent error JSON shape
│       ├── mapper/
│       │   └── DtoMapper.java         # All entity → DTO conversions (SRP)
│       ├── service/
│       │   ├── IContentService.java   # Service interfaces (DIP)
│       │   ├── IGameService.java
│       │   ├── IUserService.java
│       │   └── SlugGeneratorService.java
│       └── ...
│   └── src/main/resources/
│       ├── application.properties          # Shared defaults; sets active profile to dev
│       ├── application-dev.properties      # Dev secrets — GITIGNORED
│       └── application-prod.properties     # Prod — all values from ${ENV_VAR}
│
└── docker-compose.yml
```

---

## Twitch Setup

1. Go to [https://dev.twitch.tv/console](https://dev.twitch.tv/console)
2. Create a new application
3. Set redirect URI to `http://localhost:3000`
4. Copy the **Client ID** into `application-dev.properties` and `.env`
5. Generate an **App Access Token** (Client Credentials flow) and put it in `application-dev.properties`
6. Set `VITE_TWITCH_CHANNEL` in `.env` to your Twitch channel name

---

## Build for Production

### Docker Deployment (Recommended)

The entire application is containerized and ready for production:

```bash
# Build all Docker images
docker-compose build

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f
```

Services automatically start in the correct order with health checks:
- PostgreSQL 15 database
- Spring Boot 4.0.3 backend (depends on database)
- React 19 frontend with Nginx (depends on backend)

For detailed Docker configuration, environment variables, and production best practices, see [DOCKER.md](DOCKER.md).

### Traditional Deployment

If not using Docker:

**Frontend**

```bash
cd frontend
npm run build
# Output in frontend/dist/ — serve with any static host (Nginx, Apache, etc.)
```

**Backend**

```bash
cd backend
mvn clean package -DskipTests
# Output: backend/target/streamroom-backend-1.0.0.jar
```

Run in production mode (all secrets via environment variables):

```bash
java -jar target/streamroom-backend-1.0.0.jar \
  --spring.profiles.active=prod \
  --spring.datasource.url=jdbc:postgresql://db-host:5432/streamroom \
  --spring.datasource.username=streamroom_user \
  --spring.datasource.password=your-password \
  --jwt.secret=your-secret \
  --twitch.api.client-id=your-client-id \
  --twitch.api.access-token=your-access-token \
  --cors.allowed-origins=https://yourdomain.com
```

---

## Customisation

### Profile info

Edit [frontend/src/pages/Profile.jsx](frontend/src/pages/Profile.jsx) — update the name, bio, schedule, and social links directly.

### Theme colours

CSS variables are defined in [frontend/src/styles/globals.css](frontend/src/styles/globals.css):

```css
--cyberpunk-dark:           #0a0a0a
--cyberpunk-dark-light:     #1a1a2e
--cyberpunk-accent:         #00FFFF   /* cyan */
--cyberpunk-accent-alt:     #FF1493   /* magenta */
--cyberpunk-accent-tertiary:#6B0080   /* purple */
```

### Logo

Replace `frontend/public/logo.png` with your own image. The header and profile page both reference `/logo.png`.

---

## Troubleshooting

| Problem | Fix |
|---|---|
| `application-dev.properties not found` | Run the `cp` command in Step 2a |
| Database connection refused | Confirm PostgreSQL or Docker is running on port 5432 |
| CORS errors in browser | Check `cors.allowed-origins` in `application-dev.properties` matches your frontend URL |
| Twitch player shows blank | Set `VITE_TWITCH_CHANNEL` in `.env` to a valid live channel |
| IDE shows "cannot resolve" errors | Run `mvn compile` or reload Maven project — Lombok annotations require annotation processing |
| Port already in use | Frontend: change port in `vite.config.js`; Backend: set `server.port` in properties |
