FROM gradle:8.11.1-jdk21 AS builder
WORKDIR /app

COPY . .
RUN ./gradlew clean build bootJar -x test --no-daemon

FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=builder /app/roomy-*/build/libs/*.jar /app/

ENV APP_NAME=roomy-api
CMD ["sh", "-c", "java -jar /app/$APP_NAME.jar"]