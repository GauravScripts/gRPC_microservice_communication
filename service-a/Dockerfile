# Multi-stage build for Service A
# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Copy pom and resolve dependencies (layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Copy source
COPY src ./src
# Build (skip tests for faster container build)
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copy built jar
COPY --from=build /app/target/service-a-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
