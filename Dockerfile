# Multi-stage build for optimized image
FROM gradle:8.5-jdk21 AS build

# Set working directory
WORKDIR /app

# Copy gradle files
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Copy source code
COPY src ./src
COPY .env ./.env

# Build the application
RUN ./gradlew bootJar --no-daemon

# Production stage
FROM eclipse-temurin:21-jre-jammy

# Set working directory
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Create non-root user for security
RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/auth/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
