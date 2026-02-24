# StreamRoom - Cyberpunk Streaming Website

A full-stack streaming website with React 19 frontend and Spring Boot 3.2 backend, featuring Twitch integration and a content management system with a cyberpunk aesthetic.

## Project Structure

```
streamroom/
├── frontend/              # React 19 frontend application
│   ├── src/
│   ├── public/
│   ├── package.json
│   ├── vite.config.js
│   ├── tailwind.config.js
│   └── README.md
├── backend/               # Spring Boot backend application
│   ├── src/
│   ├── pom.xml
│   ├── README.md
│   └── .gitignore
└── README.md              # This file
```

## Quick Start

### Frontend

```bash
cd frontend
npm install
npm run dev
# Open http://localhost:5173
```

### Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
# API available at http://localhost:8080/api
```

### Database

PostgreSQL setup:

```bash
# Create database
psql -U postgres -c "CREATE DATABASE streamroom;"
psql -U postgres -c "CREATE USER streamroom_user WITH PASSWORD 'streamroom_password';"
psql -U postgres -c "ALTER ROLE streamroom_user SET client_encoding TO 'utf8';"
```

## Features

### Frontend
- ✅ Responsive cyberpunk UI theme
- ✅ Twitch stream embed with live chat
- ✅ Content listing and detail pages
- ✅ Game showcase
- ✅ Featured content display
- ✅ Mobile-responsive navigation

### Backend
- ✅ RESTful API with Spring Boot
- ✅ PostgreSQL database integration
- ✅ Content management endpoints
- ✅ Game library management
- ✅ User management system
- ✅ Stream session tracking
- ✅ CORS configuration

### CMS
- ✅ Create/Read/Update/Delete content
- ✅ Publish/Draft content
- ✅ Featured content management
- ✅ Category organization
- ✅ Content by author
- ✅ Slug-based routing

## Technology Stack

### Frontend
- React 19
- React Router v6
- Axios
- Tailwind CSS
- Vite
- React Icons

### Backend
- Java 25
- Spring Boot 3.2
- Spring Data JPA
- PostgreSQL
- Lombok
- JWT Authentication
- Spring WebFlux

## Configuration

### Frontend Environment Variables (.env)
```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_TWITCH_CLIENT_ID=your_twitch_client_id
VITE_TWITCH_CHANNEL=your_channel_name
```

### Backend Configuration (application.properties)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/streamroom
spring.datasource.username=streamroom_user
spring.datasource.password=streamroom_password
twitch.api.client-id=your_twitch_client_id
twitch.api.access-token=your_twitch_access_token
```

## API Documentation

See [Backend README](./backend/README.md) for detailed API endpoints.

## Development

### Frontend Development
- Hot module reload
- Tailwind CSS purging
- Vite development server

### Backend Development
- Maven for dependency management
- Spring Boot development tools
- Lombok for reduced boilerplate
- Auto schema updates with Hibernate

## Future Enhancements

- [ ] User authentication and authorization
- [ ] Admin dashboard
- [ ] Twitch API real-time integration
- [ ] Stream statistics and analytics
- [ ] User comments and interactions
- [ ] Image upload functionality
- [ ] Video content support
- [ ] Advanced search and filtering
- [ ] Newsletter subscription
- [ ] Social media integration

## License

MIT

## Support

For issues and questions, please open an issue in the repository.
