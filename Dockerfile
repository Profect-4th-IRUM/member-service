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

RUN curl -L \
  https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar \
  -o /app/opentelemetry-javaagent.jar

COPY --from=build /app/build/libs/*.jar app.jar

ENV TZ=Asia/Seoul

ENTRYPOINT ["java","-javaagent:/app/opentelemetry-javaagent.jar","-Dotel.service.name=product-service","-Dotel.propagators=tracecontext,baggage,b3,b3multi","-Dotel.traces.exporter=otlp","-Dotel.logs.exporter=none","-Dotel.metrics.exporter=none","-Dotel.exporter.otlp.endpoint=http://otel-collector.istio-system.svc.cluster.local:4317","-Dotel.exporter.otlp.protocol=grpc","-jar", "/app/app.jar"]
