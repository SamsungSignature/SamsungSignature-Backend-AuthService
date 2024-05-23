FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
WORKDIR /app
COPY ${JAR_FILE} auth-service.jar
ENTRYPOINT ["java", "-jar", "/app/auth-service.jar"]