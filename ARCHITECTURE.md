# 🏗️ Architecture Overview

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                          Docker Host                                 │
│                                                                       │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │                    Docker Network (grpc-network)                │ │
│  │                                                                  │ │
│  │                                                                  │ │
│  │   ┌──────────────────────────┐    ┌──────────────────────────┐ │ │
│  │   │     Service A Container  │    │    Service B Container   │ │ │
│  │   │                          │    │                          │ │ │
│  │   │  ┌────────────────────┐  │    │  ┌────────────────────┐ │ │ │
│  │   │  │  Spring Boot App   │  │    │  │  Spring Boot App   │ │ │ │
│  │   │  │                    │  │    │  │                    │ │ │ │
│  │   │  │  - REST API        │  │    │  │  - REST API        │ │ │ │
│  │   │  │  - gRPC Client     │──┼────┼─▶│  - gRPC Server     │ │ │ │
│  │   │  │  - Port: 8080      │  │    │  │  - Port: 8081      │ │ │ │
│  │   │  │                    │  │    │  │  - gRPC: 9090      │ │ │ │
│  │   │  └────────────────────┘  │    │  └────────────────────┘ │ │ │
│  │   │                          │    │                          │ │ │
│  │   │  Environment:            │    │  Environment:            │ │ │
│  │   │  - Java 21 JRE           │    │  - Java 21 JRE           │ │ │
│  │   │  - service-a.jar         │    │  - service-b.jar         │ │ │
│  │   └────────────┬─────────────┘    └────────────┬─────────────┘ │ │
│  │                │                                │               │ │
│  └────────────────┼────────────────────────────────┼───────────────┘ │
│                   │                                │                 │
│         Port Mapping                     Port Mapping               │
│         8080:8080                        8081:8081                  │
│                   │                      9090:9090                  │
└───────────────────┼────────────────────────────────┼─────────────────┘
                    │                                │
                    ▼                                ▼
            ┌───────────────┐              ┌───────────────┐
            │ Host: 8080    │              │ Host: 8081    │
            │ (External)    │              │ Host: 9090    │
            └───────────────┘              └───────────────┘
                    ▲
                    │
            ┌───────┴────────┐
            │  Client (curl, │
            │  browser, etc) │
            └────────────────┘
```

## Communication Flow

### REST API Call to Service A

```
1. Client Request
   ↓
2. localhost:8080/api/employees
   ↓
3. Service A receives REST request
   ↓
4. Service A makes gRPC call to Service B
   ↓
5. gRPC request: service-b:9090
   ↓
6. Service B processes gRPC request
   ↓
7. Service B returns gRPC response
   ↓
8. Service A converts to REST response
   ↓
9. Client receives REST response
```

## Technology Stack Flow

### Build Time (Docker Build)

```
┌─────────────────────────────────────────────────┐
│ Stage 1: Build Stage                            │
│ Image: maven:3.9-eclipse-temurin-21             │
├─────────────────────────────────────────────────┤
│                                                 │
│  1. Copy pom.xml                                │
│  2. Download Maven dependencies                 │
│  3. Copy source code & .proto files             │
│  4. Maven executes protobuf-maven-plugin        │
│     ├─ Generate Java classes from .proto       │
│     └─ Place in target/generated-sources/       │
│  5. Compile all Java code                       │
│  6. Run tests (optional)                        │
│  7. Package into service-x.jar                  │
│                                                 │
│  Output: service-x.jar (~50MB)                  │
└─────────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────┐
│ Stage 2: Runtime Stage                          │
│ Image: eclipse-temurin:21-jre-jammy             │
├─────────────────────────────────────────────────┤
│                                                 │
│  1. Copy service-x.jar from build stage         │
│  2. Configure entry point                       │
│                                                 │
│  Final Image Size: ~200MB                       │
│  (vs ~700MB if using full Maven image)          │
└─────────────────────────────────────────────────┘
```

### Runtime (Docker Container Running)

```
┌─────────────────────────────────────────────────┐
│ Container Startup                               │
├─────────────────────────────────────────────────┤
│                                                 │
│  1. JVM starts                                  │
│  2. Spring Boot initializes                     │
│  3. Load application.yml / .properties          │
│  4. Initialize gRPC server (Service B)          │
│     └─ Bind to port 9090                        │
│  5. Initialize gRPC client (Service A)          │
│     └─ Connect to service-b:9090                │
│  6. Start web server (Tomcat)                   │
│     └─ Bind to port 8080/8081                   │
│  7. Application ready ✓                         │
│                                                 │
└─────────────────────────────────────────────────┘
```

## Data Flow: Protocol Buffers

### Proto File → Java Classes

```
┌────────────────────────────────────────────────────────┐
│ employee.proto                                         │
├────────────────────────────────────────────────────────┤
│                                                        │
│  syntax = "proto3";                                    │
│                                                        │
│  message Employee {                                    │
│    int32 id = 1;                                       │
│    string name = 2;                                    │
│    string email = 3;                                   │
│  }                                                     │
│                                                        │
│  service EmployeeService {                             │
│    rpc GetEmployee(EmployeeRequest)                    │
│        returns (Employee);                             │
│  }                                                     │
└────────────────────────────────────────────────────────┘
                    ↓
        protobuf-maven-plugin executes
                    ↓
┌────────────────────────────────────────────────────────┐
│ Generated Java Classes                                 │
│ (target/generated-sources/protobuf/)                   │
├────────────────────────────────────────────────────────┤
│                                                        │
│  ├─ Employee.java                                      │
│  ├─ EmployeeOrBuilder.java                             │
│  ├─ EmployeeRequest.java                               │
│  ├─ EmployeeServiceGrpc.java                           │
│  │   ├─ EmployeeServiceBlockingStub                    │
│  │   ├─ EmployeeServiceFutureStub                      │
│  │   └─ EmployeeServiceImplBase                        │
│  └─ EmployeeProto.java                                 │
│                                                        │
└────────────────────────────────────────────────────────┘
                    ↓
            Compiled with rest of code
                    ↓
            Packaged into JAR
```

## Network Communication Detail

### Service Discovery in Docker

```
┌──────────────────────────────────────────────────────┐
│ Docker Network: grpc-network                         │
├──────────────────────────────────────────────────────┤
│                                                      │
│  Docker DNS Resolution:                              │
│                                                      │
│  service-a → 172.18.0.2 (example)                    │
│  service-b → 172.18.0.3 (example)                    │
│                                                      │
│  When Service A connects to "service-b:9090":        │
│  1. Docker DNS resolves "service-b" → 172.18.0.3     │
│  2. TCP connection established                       │
│  3. gRPC communication over HTTP/2                   │
│                                                      │
└──────────────────────────────────────────────────────┘
```

### gRPC Communication

```
Service A                              Service B
   │                                      │
   │  1. Create gRPC Channel              │
   │     to service-b:9090                │
   ├──────────────────────────────────────▶
   │                                      │
   │  2. HTTP/2 Connection                │
   ◀──────────────────────────────────────┤
   │     Established                      │
   │                                      │
   │  3. gRPC Request (Binary)            │
   │     Protobuf Encoded                 │
   ├──────────────────────────────────────▶
   │                                      │
   │                                      │  4. Process Request
   │                                      │  5. Create Response
   │                                      │
   │  6. gRPC Response (Binary)           │
   │     Protobuf Encoded                 │
   ◀──────────────────────────────────────┤
   │                                      │
   │  7. Decode Response                  │
   │                                      │
```

## Maven Build Lifecycle

```
┌─────────────────────────────────────────────────────┐
│ mvn clean package                                   │
├─────────────────────────────────────────────────────┤
│                                                     │
│  1. clean                                           │
│     └─ Delete target/ directory                     │
│                                                     │
│  2. validate                                        │
│     └─ Check project structure                      │
│                                                     │
│  3. generate-sources ⭐                             │
│     └─ protobuf-maven-plugin:generate               │
│        ├─ Read .proto files                         │
│        ├─ Generate Java classes                     │
│        └─ Output to target/generated-sources/       │
│                                                     │
│  4. process-sources                                 │
│     └─ Process generated source code                │
│                                                     │
│  5. generate-resources                              │
│     └─ Generate additional resources                │
│                                                     │
│  6. process-resources                               │
│     └─ Copy resources to target/classes/            │
│                                                     │
│  7. compile ⭐                                      │
│     └─ Compile all Java code                        │
│        ├─ Your source code                          │
│        └─ Generated Protocol Buffer classes         │
│                                                     │
│  8. test                                            │
│     └─ Run unit tests                               │
│                                                     │
│  9. package ⭐                                      │
│     └─ Create service-x.jar                         │
│        ├─ Compiled classes                          │
│        ├─ Resources                                 │
│        └─ Dependencies                              │
│                                                     │
└─────────────────────────────────────────────────────┘
```

## File Organization

```
Project Root
│
├── service-a/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/              ← Your Java code
│   │   │   ├── proto/             ← .proto definitions
│   │   │   └── resources/         ← Config files
│   │   └── test/                  ← Test code
│   │
│   ├── target/                    ← Maven output
│   │   ├── classes/               ← Compiled .class files
│   │   ├── generated-sources/     ← ⭐ Generated Proto classes
│   │   │   └── protobuf/
│   │   │       └── com/example/
│   │   └── service-a.jar          ← Final artifact
│   │
│   ├── Dockerfile                 ← Container definition
│   ├── .dockerignore              ← Docker build exclusions
│   └── pom.xml                    ← Maven configuration
│
├── service-b/                     ← Same structure
│
└── docker-compose.yml             ← Orchestration
```

## Summary

### Key Points

1. **Protocol Buffers**: `.proto` files → Maven plugin → Java classes → JAR
2. **Docker Build**: Multi-stage build → Optimized image → Small footprint
3. **Communication**: Service A → gRPC → Service B (internal Docker network)
4. **Deployment**: Single command → Both services running → Production ready

### Why This Architecture Works

✅ **Separation of Concerns**: Each service has its own container
✅ **Scalability**: Services can be scaled independently
✅ **Maintainability**: Clear structure and documentation
✅ **Performance**: gRPC binary protocol, HTTP/2, small images
✅ **Development**: Docker ensures consistency across environments
✅ **Automation**: No manual steps, everything is automated

---

This architecture follows microservices best practices and is production-ready! 🚀

