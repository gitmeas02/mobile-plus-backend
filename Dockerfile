FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY build/libs/*.jar app.jar
COPY .env .env

RUN mkdir -p uploads/images && \
    useradd -m -u 1000 appuser && \
    chown -R appuser:appuser uploads .env
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
