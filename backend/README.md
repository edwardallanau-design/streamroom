# StreamRoom Backend

A Spring Boot 3.2 backend for a Twitch streaming website with content management system.

## Features

- RESTful API for content management
- Twitch integration
- User management
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

Update `src/main/resources/application.properties` with your database credentials and Twitch API keys.

### 4. Build

```bash
mvn clean install
```

### 5. Run

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080/api`

## API Endpoints

### Content
- `GET /api/content` - Get all published content
- `GET /api/content/{id}` - Get content by ID
- `GET /api/content/slug/{slug}` - Get content by slug
- `GET /api/content/featured` - Get featured content
- `POST /api/content` - Create content
- `PUT /api/content/{id}` - Update content
- `DELETE /api/content/{id}` - Delete content

### Games
- `GET /api/games` - Get all games
- `GET /api/games/{id}` - Get game by ID
- `GET /api/games/featured` - Get featured games
- `POST /api/games` - Create game
- `PUT /api/games/{id}` - Update game
- `DELETE /api/games/{id}` - Delete game

### Health
- `GET /api/health` - Check API health

## Project Structure

```
src/
├── main/
│   ├── java/com/streamroom/
│   │   ├── controller/    # API endpoints
│   │   ├── service/       # Business logic
│   │   ├── repository/    # Data access
│   │   ├── entity/        # JPA entities
│   │   ├── dto/           # Data transfer objects
│   │   ├── config/        # Configuration
│   │   └── StreamroomApplication.java
│   └── resources/
│       └── application.properties
└── test/
```

## Technologies

- Spring Boot 3.2
- Spring Data JPA
- PostgreSQL
- Lombok
- JWT (io.jsonwebtoken)
- Spring WebFlux

## License

MIT
