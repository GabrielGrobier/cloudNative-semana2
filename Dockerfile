# Dockerfile multiplataforma: ambas imágenes (maven:3.9.6-eclipse-temurin-17 y eclipse-temurin:17-jre) soportan linux/amd64 y linux/arm64
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app
COPY --from=build /app/target/mini-s3-springboot-example-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
