# 1) Build Stage
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# 전체 프로젝트 복사
COPY . .

# 실행 권한
RUN chmod +x ./gradlew

# domain 모듈만 빌드
RUN ./gradlew :domain:build -x test --no-daemon

# 2) Runtime Stage
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/domain/build/libs/domain-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

#FROM eclipse-temurin:21-jre
#WORKDIR /app
#
## 로컬에서 미리 빌드된 JAR 파일 복사
#COPY domain/build/libs/domain-0.0.1-SNAPSHOT.jar app.jar
#
#
#ENTRYPOINT ["java", "-jar", "app.jar"]