# syntax=docker/dockerfile:1.2

FROM gradle:8.7-jdk21-alpine AS build

ARG GIT_USERNAME
ARG GIT_TOKEN

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle

COPY . .
RUN GIT_USERNAME=$GIT_USERNAME GIT_TOKEN=$GIT_TOKEN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /app/build/libs/*SNAPSHOT.jar app.jar

ENV TZ=Asia/Seoul

ENTRYPOINT ["java", "-jar", "app.jar"]
