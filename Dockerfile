FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY build/libs/*.jar app.jar

RUN mkdir -p uploads/images && \
    useradd -m -u 1000 appuser && \
    chown -R appuser:appuser uploads
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
