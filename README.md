# 🚀 gRPC Communication Example with Spring Boot

A complete example of microservices communication using **gRPC** and **Spring Boot 3.5**. This project demonstrates bidirectional communication between two services using Protocol Buffers and gRPC.

## 📋 Overview

This repository contains two microservices:

- **Service A** (Port 8080): Acts as a gRPC client, makes REST API calls and communicates with Service B via gRPC
- **Service B** (Port 8081/9090): Acts as a gRPC server, exposes gRPC services on port 9090

## 🏗️ Architecture

```
┌─────────────┐         REST/HTTP          ┌─────────────┐
│   Client    │ ────────────────────────> │  Service A  │
└─────────────┘                            │  (Port 8080)│
                                           └──────┬──────┘
                                                  │
                                                  │ gRPC
                                                  │
                                           ┌──────▼──────┐
                                           │  Service B  │
                                           │ (Port 8081) │
                                           │ gRPC: 9090  │
                                           └─────────────┘
```

## 🛠️ Technology Stack

- **Java 21**
- **Spring Boot 3.5.7**
- **Spring gRPC 0.12.0**
- **gRPC 1.76.0**
- **Protocol Buffers 4.32.1**
- **Maven**
- **Docker & Docker Compose**

## 📦 Project Structure

```
gRPC-communication/
├── service-a/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/servicea/
│   │   │   ├── proto/                    # Protocol Buffer definitions
│   │   │   │   ├── employee.proto
│   │   │   │   └── hello.proto
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── test/
│   ├── Dockerfile
│   ├── .dockerignore
│   └── pom.xml
├── service-b/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/org/example/serviceb/
│   │   │   ├── proto/                    # Protocol Buffer definitions
│   │   │   │   ├── employee.proto
│   │   │   │   └── hello.proto
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   ├── Dockerfile
│   ├── .dockerignore
│   └── pom.xml
├── docker-compose.yml
└── README.md
```

## 🚀 Quick Start with Docker

### Prerequisites

- Docker Desktop installed
- Docker Compose installed
- No need for Java or Maven on your local machine!

### Run with Docker Compose

```bash
# Clone the repository
git clone <your-repo-url>
cd "gRPC communication"

# Build and start all services
docker-compose up --build

# Or run in detached mode
docker-compose up -d --build

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Verify Services

```bash
# Check Service A (REST endpoint)
curl http://localhost:8080/

# Check Service B (REST endpoint)
curl http://localhost:8081/

# Service A will communicate with Service B via gRPC internally
```

## 💻 Local Development (Without Docker)

### Prerequisites

- Java 21
- Maven 3.6+

### Build and Run

```bash
# Terminal 1 - Start Service B first (gRPC Server)
cd service-b
mvn clean install
mvn spring-boot:run

# Terminal 2 - Start Service A (gRPC Client)
cd service-a
mvn clean install
mvn spring-boot:run
```

## 🔧 Protocol Buffers

### Why Generated Code is in `target/`?

Protocol Buffer files (`.proto`) are compiled by the `protobuf-maven-plugin` during the Maven build process. The generated Java classes are placed in:
```
target/generated-sources/protobuf/
```

**Important Notes:**
- ✅ You **DO NOT** need to manually mark `target/generated-sources` as a source directory
- ✅ The Maven plugin automatically adds it to the build path during compilation
- ✅ Your IDE (IntelliJ IDEA) might need a manual refresh: `File > Invalidate Caches > Restart`
- ✅ In Docker builds, this happens automatically during the build stage

### Maven Plugin Configuration

The `pom.xml` includes:
```xml
<plugin>
    <groupId>io.github.ascopes</groupId>
    <artifactId>protobuf-maven-plugin</artifactId>
    <version>3.8.0</version>
    <configuration>
        <protocVersion>${protobuf-java.version}</protocVersion>
        <binaryMavenPlugins>
            <binaryMavenPlugin>
                <groupId>io.grpc</groupId>
                <artifactId>protoc-gen-grpc-java</artifactId>
                <version>${grpc.version}</version>
            </binaryMavenPlugin>
        </binaryMavenPlugins>
    </configuration>
</plugin>
```

## 🐳 Docker Configuration

### Multi-stage Builds

Both services use **multi-stage Docker builds** for optimal image size:

1. **Build Stage**: Uses `maven:3.9-eclipse-temurin-21` to compile the application
2. **Runtime Stage**: Uses `eclipse-temurin:21-jre-jammy` (smaller JRE-only image)

Benefits:
- ✅ Smaller final image size (~200MB vs ~700MB)
- ✅ No Maven or source code in production image
- ✅ Faster deployment
- ✅ Better security (fewer dependencies)

### Docker Networking

Services communicate through a dedicated Docker network (`grpc-network`):
- Service B is accessible at `service-b:9090` for gRPC
- Environment variable `GRPC_CLIENT_SERVICE-B-GRPC_ADDRESS=static://service-b:9090` configures the connection

## 📊 API Endpoints

### Service A (Port 8080)
- `GET /api/employees` - Fetch employees via gRPC from Service B
- `POST /api/employees` - Create employee via gRPC to Service B
- `GET /api/hello` - Test gRPC connection

### Service B (Port 8081)
- `GET /health` - Health check endpoint
- gRPC Server on port **9090**

## 🔍 Troubleshooting

### Issue: Generated classes not found in IDE

**Solution:**
```bash
# Run Maven compile
mvn clean compile

# In IntelliJ IDEA
File > Invalidate Caches > Restart
```

### Issue: Docker build fails

**Solution:**
```bash
# Clean Docker cache
docker-compose down
docker system prune -a

# Rebuild
docker-compose up --build
```

### Issue: gRPC connection refused

**Solution:**
- Ensure Service B starts before Service A
- Check `docker-compose logs service-b` for errors
- Verify network configuration in docker-compose.yml

## 📝 Configuration

### Service A (`application.yml`)
```yaml
grpc:
  client:
    service-b-grpc:
      address: 'static://localhost:9090'  # Local development
      # address: 'static://service-b:9090' # Docker

server:
  port: 8080
```

### Service B (`application.properties`)
```properties
spring.application.name=service-b
server.port=8081
# gRPC port is configured by Spring gRPC (default: 9090)
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## ⭐ Show Your Support

If you find this project helpful, please give it a ⭐️!

## 📧 Contact

For questions or feedback, please open an issue on GitHub.

---

**Happy Coding!** 🎉

