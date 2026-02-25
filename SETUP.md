# StreamRoom — Setup Guide

## Prerequisites

- **Node.js** 18+ and npm
- **Java 25** and Maven 3.9+
- **PostgreSQL 15+** (or Docker)

---

## Step 1 — Database

**Option A: Docker (recommended)**

```bash
docker-compose up -d
```

This starts a PostgreSQL 15 container on port `5432` with database `streamroom`, user `streamroom_user`, password `streamroom_password`.

**Option B: Manual PostgreSQL**

```bash
psql -U postgres -c "CREATE DATABASE streamroom;"
psql -U postgres -c "CREATE USER streamroom_user WITH PASSWORD 'streamroom_password';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE streamroom TO streamroom_user;"
```

---

## Step 2 — Backend

### 2a. Configure secrets

The backend uses Spring profiles. The `dev` profile reads from `application-dev.properties`, which is gitignored so your credentials are never committed.

Create the file from the provided example:

```bash
cp backend/src/main/resources/application.properties.example \
   backend/src/main/resources/application-dev.properties
```

Then open `application-dev.properties` and fill in your values:

```properties
# Database (defaults work if you used Docker in Step 1)
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

---

## Step 3 — Frontend

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

### Frontend

```bash
cd frontend
npm run build
# Output in frontend/dist/ — serve with any static host
```

### Backend

```bash
cd backend
mvn clean package -DskipTests
# Output: backend/target/streamroom-backend-1.0.0.jar
```

Run in production mode (all secrets via environment variables):

```bash
java -jar target/streamroom-backend-1.0.0.jar \
  --spring.profiles.active=prod \
  --DB_URL=jdbc:postgresql://... \
  --DB_USERNAME=... \
  --DB_PASSWORD=... \
  --JWT_SECRET=... \
  --TWITCH_CLIENT_ID=... \
  --TWITCH_ACCESS_TOKEN=... \
  --CORS_ALLOWED_ORIGINS=https://yourdomain.com
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
