# Multi-stage build for Service B
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml and download dependencies (layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and proto files
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/service-b-*.jar app.jar

# Expose HTTP port and gRPC port
EXPOSE 8081
EXPOSE 9090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

