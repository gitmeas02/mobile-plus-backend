# Lightweight runtime image (NO Gradle, NO build stage)
FROM eclipse-temurin:21-jre-jammy

# Set working directory
WORKDIR /app

# Copy JAR built locally
COPY build/libs/*.jar app.jar

# Create non-root user for security
RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app
USER appuser

# Expose port
EXPOSE 8085

# Health check (FIXED PORT)
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8085/auth/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
# then run : gradlew bootJar