# Build stage
FROM maven:3.9-eclipse-temurin-25 AS builder

WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:25-jdk-alpine

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/streamroom-backend-1.0.0.jar app.jar

# Expose port 8080
EXPOSE 8080

# Set environment variables
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/streamroom
ENV SPRING_DATASOURCE_USERNAME=streamroom_user
ENV SPRING_DATASOURCE_PASSWORD=streamroom_password

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
