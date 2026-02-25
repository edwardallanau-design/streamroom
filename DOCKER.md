# StreamRoom Docker Deployment

## Overview
The entire StreamRoom application (frontend, backend, and database) is now containerized and ready for deployment.

## Services
- **Frontend**: React 19 with Vite, served by Nginx
- **Backend**: Spring Boot 4.0.3 with Java 25
- **Database**: PostgreSQL 15

## Quick Start

### Build and Run All Services
```bash
docker-compose up --build
```

The application will be available at:
- **Frontend**: http://localhost
- **Backend API**: http://localhost/api
- **Database**: localhost:5432

### Stop All Services
```bash
docker-compose down
```

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f frontend
docker-compose logs -f backend
docker-compose logs -f postgres
```

## Configuration

### Environment Variables
The following environment variables can be set in docker-compose.yml:

**Backend**:
- `SPRING_DATASOURCE_URL`: Database connection string
- `SPRING_DATASOURCE_USERNAME`: Database user
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: Hibernate DDL mode (update/create/validate)

### Port Mapping
- Frontend: 80 (Nginx)
- Backend: 8080 (Spring Boot)
- Database: 5432 (PostgreSQL)

## Architecture

### Service Networking
Services communicate via Docker network `streamroom-network`:
- Backend connects to PostgreSQL via `postgres:5432`
- Frontend (Nginx) proxies API requests to `backend:8080`
- Both frontend and backend are on the same network

### Health Checks
- PostgreSQL has health checks enabled
- Backend depends on healthy PostgreSQL before starting
- Frontend depends on Backend service

## Development

### Local Development (Non-Docker)
For local development without Docker:

```bash
# Terminal 1: Start database
docker-compose up postgres

# Terminal 2: Start backend
cd backend && mvn spring-boot:run

# Terminal 3: Start frontend
cd frontend && npm run dev
```

Frontend will be available at http://localhost:5173

## Production Deployment

1. Build images:
   ```bash
   docker-compose build
   ```

2. Run services:
   ```bash
   docker-compose up -d
   ```

3. Monitor logs:
   ```bash
   docker-compose logs -f
   ```

4. Stop services:
   ```bash
   docker-compose down
   ```

## Troubleshooting

### Backend Cannot Connect to Database
Ensure PostgreSQL is healthy:
```bash
docker-compose ps
```

Check logs:
```bash
docker-compose logs postgres
```

### Frontend API Requests Failing
Check Nginx is proxying correctly:
```bash
docker-compose logs frontend
```

Verify backend is running and healthy:
```bash
docker-compose logs backend
```

## Building Individual Images

### Backend
```bash
cd backend
docker build -t streamroom-backend:latest .
```

### Frontend
```bash
cd frontend
docker build -t streamroom-frontend:latest .
```

## Image Sizes (Approximate)
- Backend: ~400MB (Java 25 + Spring Boot + dependencies)
- Frontend: ~50MB (Node build + Nginx alpine)
- PostgreSQL: ~150MB (Alpine version)
