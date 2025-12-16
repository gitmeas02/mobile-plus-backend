# =========================
# Build stage
# =========================
FROM gradle:8.5-jdk21 AS build

WORKDIR /app

# Copy Gradle wrapper and config
COPY gradlew .
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Copy source code
COPY src ./src
COPY .env ./.env

# Make Gradle wrapper executable
RUN chmod +x gradlew

# Build the application (skip tests for speed)
RUN ./gradlew bootJar --no-daemon -x test

# =========================
# Production stage
# =========================
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy the built JAR
COPY --from=build /app/build/libs/*.jar app.jar


# Create non-root user
RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app
USER appuser

# Expose the port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/auth/health || exit 1

# Run the app on port 8080 (change if needed)
ENTRYPOINT ["java", "-jar", "app.jar"]
# when update new thing we have to gradlew build first to generate new jar file
# then docker build -t mobile_plus_backend .
# then docker-compose up -d --build to rebuild the container with new jar file
