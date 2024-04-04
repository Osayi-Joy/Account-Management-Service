# Stage 1: Build environment
FROM gradle:8.7.0-jdk17-alpine AS builder
WORKDIR /workspace
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle clean build -x test

# Stage 2: Create a minimal runtime image
FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=/workspace/build/libs/*.jar
COPY --from=builder ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]