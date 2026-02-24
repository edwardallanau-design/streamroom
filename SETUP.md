# StreamRoom - Setup & Getting Started Guide

## 📋 What's Included

Your StreamRoom project has been fully scaffolded with:

### Frontend (React 19 + Vite)
- ✅ Modern React 19 setup with Vite bundler
- ✅ Cyberpunk-themed UI with Tailwind CSS
- ✅ React Router for page navigation
- ✅ Axios API client
- ✅ Twitch stream embed with chat
- ✅ Responsive components

### Backend (Spring Boot 3.2 + Java 25)
- ✅ RESTful API endpoints
- ✅ Content Management System (CMS)
- ✅ Game library management
- ✅ User management
- ✅ Stream session tracking
- ✅ PostgreSQL database integration

### Database
- ✅ PostgreSQL configuration
- ✅ Docker Compose setup for easy local development
- ✅ Automated schema creation with Hibernate

## 🚀 Quick Start

### Step 1: Setup Database

**Option A: Using Docker (Recommended)**
```bash
cd streamroom
docker-compose up -d
```

**Option B: Manual PostgreSQL Setup**
```bash
# Create database
createdb streamroom

# Create user
psql -c "CREATE USER streamroom_user WITH PASSWORD 'streamroom_password';"

# Grant privileges
psql -c "ALTER ROLE streamroom_user SET client_encoding TO 'utf8';"
psql -c "ALTER ROLE streamroom_user SET default_transaction_isolation TO 'read committed';"
psql -c "ALTER ROLE streamroom_user SET default_transaction_deferrable TO on;"
psql -c "ALTER ROLE streamroom_user SET timezone TO 'UTC';"
psql -c "GRANT ALL PRIVILEGES ON DATABASE streamroom TO streamroom_user;"
```

### Step 2: Setup Backend

```bash
cd backend

# Copy example configuration
cp src/main/resources/application.properties.example src/main/resources/application.properties

# Build
mvn clean install

# Run
mvn spring-boot:run
```

Backend will be available at: `http://localhost:8080/api`

### Step 3: Setup Frontend

In a new terminal:

```bash
cd frontend

# Install dependencies
npm install

# Copy environment variables
cp .env.example .env
# Edit .env and add your Twitch channel name and client ID

# Start development server
npm run dev
```

Frontend will be available at: `http://localhost:5173`

## ⚙️ Configuration

### Twitch Integration

1. Go to https://dev.twitch.tv/console
2. Create a new application
3. Add your redirect URI: `http://localhost:3000`
4. Get your Client ID
5. Update `.env` in frontend and `application.properties` in backend

### Environment Variables

**Frontend (.env)**
```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_TWITCH_CLIENT_ID=your_client_id_here
VITE_TWITCH_CHANNEL=your_channel_name
```

**Backend (application.properties)**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/streamroom
spring.datasource.username=streamroom_user
spring.datasource.password=streamroom_password
twitch.api.client-id=your_client_id_here
twitch.api.access-token=your_access_token_here
```

## 📁 Project Structure

```
streamroom/
├── frontend/
│   ├── src/
│   │   ├── components/      # React components
│   │   ├── pages/           # Page components
│   │   ├── styles/          # CSS files
│   │   ├── utils/           # Helper functions
│   │   ├── api/             # API client
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── public/
│   ├── package.json
│   ├── vite.config.js
│   ├── tailwind.config.js
│   └── index.html
│
├── backend/
│   ├── src/
│   │   ├── main/java/com/streamroom/
│   │   │   ├── controller/  # API endpoints
│   │   │   ├── service/     # Business logic
│   │   │   ├── repository/  # Data access
│   │   │   ├── entity/      # JPA entities
│   │   │   ├── dto/         # Data models
│   │   │   ├── config/      # Configuration
│   │   │   └── StreamroomApplication.java
│   │   ├── test/
│   │   └── resources/
│   │       └── application.properties
│   ├── pom.xml
│   └── README.md
│
├── docker-compose.yml       # Database setup
└── README.md
```

## 🎨 Cyberpunk Theme

The theme includes:
- **Dark Background**: `#0a0a0a`
- **Dark Secondary**: `#1a1a2e`
- **Cyan Accent**: `#00d4ff`
- **Magenta Accent**: `#ff006e`
- **Purple Accent**: `#b300ff`
- **Font**: Orbitron (monospace)

Customize colors in:
- Frontend: `frontend/src/styles/globals.css`
- Tailwind: `frontend/tailwind.config.js`

## 🔌 API Endpoints

### Content
- `GET /api/content` - Get all published content
- `GET /api/content/{id}` - Get specific content
- `GET /api/content/slug/{slug}` - Get content by slug
- `GET /api/content/featured` - Get featured content
- `POST /api/content` - Create content
- `PUT /api/content/{id}` - Update content
- `DELETE /api/content/{id}` - Delete content

### Games
- `GET /api/games` - Get all games
- `GET /api/games/{id}` - Get specific game
- `GET /api/games/featured` - Get featured games
- `POST /api/games` - Create game
- `PUT /api/games/{id}` - Update game
- `DELETE /api/games/{id}` - Delete game

### Health
- `GET /api/health` - Check API status

## 📦 Build & Deployment

### Frontend Build
```bash
cd frontend
npm run build
# Output in frontend/dist/
```

### Backend Build
```bash
cd backend
mvn clean package
# Output: backend/target/streamroom-backend-1.0.0.jar
```

## 🧪 Testing

### Frontend
```bash
cd frontend
npm run lint
```

### Backend
```bash
cd backend
mvn test
```

## 🐛 Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL is running (or Docker container)
- Check credentials in `application.properties`
- Verify database created: `psql -l`

### Port Already in Use
- Frontend: Change port in `vite.config.js` (default: 5173)
- Backend: Change port in `application.properties` (default: 8080)

### CORS Errors
- Verify frontend URL in backend CORS config: `StreamroomApplication.java`
- Check API URL in frontend `.env`

## 🎯 Next Steps

1. **Add Twitch Integration**: Update Twitch API credentials
2. **Create Admin Dashboard**: Build admin management interface
3. **User Authentication**: Implement JWT-based auth
4. **Content Upload**: Add image/video upload functionality
5. **Stream Analytics**: Track viewer stats and engagement
6. **Comments System**: Add user interactions
7. **Search & Filter**: Implement advanced search

## 📚 Useful Resources

- [React Documentation](https://react.dev)
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [PostgreSQL Docs](https://www.postgresql.org/docs)
- [Twitch API Docs](https://dev.twitch.tv/docs)
- [Tailwind CSS](https://tailwindcss.com)

## 💡 Tips

- Use `npm run dev` for hot reload during development
- Spring Boot includes automatic restart with `spring-boot-devtools`
- Tailwind CSS purges unused styles in production builds
- Database schema auto-updates via Hibernate

## 📝 Notes

- All sensitive credentials should be in `.env` files (NOT committed)
- Use `.env.example` as template for other developers
- PostgreSQL automatic schema updates in development (`ddl-auto=update`)
- CORS is configured for localhost development

## 🆘 Support

- Check README files in each directory (frontend/, backend/)
- Review Docker Compose config for database setup
- Ensure all prerequisites are installed

---

**Happy streaming! 🎮✨**
