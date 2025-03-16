# 1. Gradle 빌드 환경을 위한 JDK 기반 이미지 사용
FROM gradle:8.11.1-jdk21 AS builder
WORKDIR /app

# 2. 프로젝트 소스 복사
COPY . .

# 3. Gradle 빌드 실행 (멀티 모듈 빌드)
RUN ./gradlew clean build bootJar -x test --no-daemon

# 4. 실행 환경을 위한 JDK 경량 이미지 사용
FROM openjdk:21-jdk-slim
WORKDIR /app

# 5. 빌드된 JAR 파일 복사 (버전 없는 JAR 파일 유지)
COPY --from=builder /app/roomy-api/build/libs/roomy-api.jar /app/
COPY --from=builder /app/roomy-admin/build/libs/roomy-admin.jar /app/
COPY --from=builder /app/roomy-batch/build/libs/roomy-batch.jar /app/

# 6. 실행할 모듈 선택 (기본값: roomy-api)
ENV APP_NAME=roomy-api

# 7. 컨테이너 시작 시 실행할 JAR 선택 실행
CMD ["sh", "-c", "java -jar /app/$APP_NAME.jar"]