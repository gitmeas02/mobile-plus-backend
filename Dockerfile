FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY build/libs/*.jar app.jar

RUN mkdir -p uploads/images && \
    chown -R appuser:appuser uploads && \
    useradd -m -u 1000 appuser
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
