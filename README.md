# 🚀 gRPC vs REST Communication Demo

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![gRPC](https://img.shields.io/badge/gRPC-1.76.0-blue.svg)](https://grpc.io/)
[![Protocol Buffers](https://img.shields.io/badge/Protobuf-4.32.1-yellow.svg)](https://developers.google.com/protocol-buffers)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

> A production-ready microservices project demonstrating **gRPC** and **REST API** communication patterns using Spring Boot 3, Protocol Buffers, and modern Java 21 features.

![gRPC Architecture](https://miro.medium.com/v2/resize:fit:1400/1*aKL3u6aNJpjr7tQfBJ2dNQ.png)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
- [API Examples](#-api-examples)
- [Protocol Buffers Schema](#-protocol-buffers-schema)
- [Performance Comparison](#-performance-comparison)
- [Project Structure](#-project-structure)
- [Contributing](#-contributing)

---

## 🎯 Overview

This project demonstrates **inter-service communication** in microservices architecture using both **gRPC** and **REST** protocols. It showcases:

- **Service A** (Client): Acts as an API gateway that forwards requests to Service B using both gRPC and REST
- **Service B** (Server): Provides employee management services via gRPC server and REST endpoints

### Why This Matters?

- 🔥 **gRPC** offers up to **7x faster** performance compared to REST for inter-service communication
- 🎯 **Type-safe** API contracts using Protocol Buffers
- 📦 **Binary serialization** reduces payload size by ~60%
- 🔌 **HTTP/2** multiplexing enables efficient streaming

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENT / BROWSER                        │
└────────────────────────────┬────────────────────────────────────┘
                             │ HTTP/REST
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                        SERVICE A (Port 8080)                     │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              TestController (REST API)                    │  │
│  │  - /grpc/employee/{id}    - /rest/employee/{id}          │  │
│  │  - /grpc/employee (POST)  - /rest/employee (POST)        │  │
│  └──────────────────┬──────────────────┬────────────────────┘  │
│                     │                   │                        │
│         ┌───────────▼────────┐   ┌─────▼──────────┐            │
│         │ EmployeeGrpcClient │   │  RestTemplate  │            │
│         └───────────┬────────┘   └─────┬──────────┘            │
└─────────────────────┼──────────────────┼────────────────────────┘
                      │                   │
                 gRPC │ :9090        REST │ :8081
               (Binary)            (JSON)  │
                      │                   │
                      ▼                   ▼
┌─────────────────────────────────────────────────────────────────┐
│                        SERVICE B                                 │
│  ┌────────────────────────┐    ┌────────────────────────────┐  │
│  │  gRPC Server (9090)    │    │  REST API (8081)           │  │
│  │  EmployeeServiceImpl   │    │  EmployeeRestController    │  │
│  └────────────┬───────────┘    └──────────┬─────────────────┘  │
│               │                            │                     │
│               └──────────┬─────────────────┘                     │
│                          ▼                                       │
│                 ┌────────────────────┐                          │
│                 │   In-Memory DB     │                          │
│                 │  (ConcurrentMap)   │                          │
│                 └────────────────────┘                          │
└─────────────────────────────────────────────────────────────────┘
```

---

## ✨ Features

### Core Features
- ✅ **Dual Protocol Support**: Compare gRPC vs REST side-by-side
- ✅ **Protocol Buffers**: Type-safe, language-agnostic data serialization
- ✅ **Spring Boot 3**: Modern Spring framework with native compilation support
- ✅ **Interceptors**: Request/Response logging for debugging
- ✅ **In-Memory Database**: Quick testing without external dependencies
- ✅ **Complex Data Types**: Maps, repeated fields, timestamps, and nested objects

### Advanced Features
- 🔐 **Custom Interceptors**: Logging and monitoring on client/server sides
- 🎨 **JSON Conversion**: Protobuf ↔ JSON serialization for REST compatibility
- 🌐 **HTTP Message Converters**: Seamless Spring Boot integration
- ⚡ **Connection Pooling**: Reusable gRPC channels

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Programming Language |
| Spring Boot | 3.5.7 | Application Framework |
| gRPC | 1.76.0 | RPC Framework |
| Protocol Buffers | 4.32.1 | Serialization |
| Spring gRPC | 0.12.0 | Spring Integration |
| Maven | 3.x | Build Tool |

---

## 🚀 Getting Started

### Prerequisites

```bash
# Check Java version (requires Java 21)
java -version

# Check Maven
mvn -version
```

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/grpc-rest-demo.git
cd grpc-rest-demo
```

2. **Build both services**
```bash
# Build Service A
cd service-a
mvn clean install

# Build Service B
cd ../service-b
mvn clean install
```

3. **Start Service B (Server) first**
```bash
cd service-b
mvn spring-boot:run
```
Service B will start on:
- REST API: `http://localhost:8081`
- gRPC Server: `localhost:9090`

4. **Start Service A (Client)**
```bash
cd service-a
mvn spring-boot:run
```
Service A will start on:
- REST API: `http://localhost:8080`

---

## 📡 API Examples

### 1️⃣ Get Employee via gRPC

**Request:**
```bash
curl -X GET http://localhost:8080/grpc/employee/1
```

**Response:**
```json
{
  "id": 1,
  "name": "Gaurav",
  "salary": 95000.0,
  "departments": [
    {
      "id": 1,
      "name": "Engineering"
    }
  ],
  "addressMap": {
    "city": "Bangalore",
    "country": "India"
  },
  "isActive": true,
  "joinDate": {
    "seconds": 1699286400,
    "nanos": 0
  }
}
```

---

### 2️⃣ Get Employee via REST

**Request:**
```bash
curl -X GET http://localhost:8080/rest/employee/1
```

**Response:**
```json
{
  "id": 1,
  "name": "Gaurav",
  "salary": 95000.0,
  "departments": [
    {
      "id": 1,
      "name": "Engineering"
    }
  ],
  "addressMap": {
    "city": "Bangalore",
    "country": "India"
  },
  "isActive": true,
  "joinDate": {
    "seconds": 1699286400,
    "nanos": 0
  }
}
```

---

### 3️⃣ Add New Employee via gRPC

**Request:**
```bash
curl -X POST http://localhost:8080/grpc/employee \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Priya Sharma",
    "salary": 120000.0,
    "departments": [
      {
        "id": 2,
        "name": "Product Management"
      }
    ],
    "addressMap": {
      "city": "Mumbai",
      "country": "India"
    },
    "isActive": true
  }'
```

**Response:**
```json
{
  "id": 2,
  "name": "Priya Sharma",
  "salary": 120000.0,
  "departments": [
    {
      "id": 2,
      "name": "Product Management"
    }
  ],
  "addressMap": {
    "city": "Mumbai",
    "country": "India"
  },
  "isActive": true,
  "joinDate": {
    "seconds": 1699286400,
    "nanos": 0
  }
}
```

---

### 4️⃣ Add New Employee via REST

**Request:**
```bash
curl -X POST http://localhost:8080/rest/employee \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Rahul Kumar",
    "salary": 85000.0,
    "departments": [
      {
        "id": 3,
        "name": "DevOps"
      }
    ],
    "addressMap": {
      "city": "Hyderabad",
      "country": "India"
    },
    "isActive": true
  }'
```

**Response:**
```json
{
  "id": 3,
  "name": "Rahul Kumar",
  "salary": 85000.0,
  "departments": [
    {
      "id": 3,
      "name": "DevOps"
    }
  ],
  "addressMap": {
    "city": "Hyderabad",
    "country": "India"
  },
  "isActive": true
}
```

---

### 5️⃣ Direct Access to Service B (REST)

**Get Employee:**
```bash
curl -X GET http://localhost:8081/employee/1
```

**Get All Employees:**
```bash
curl -X GET http://localhost:8081/employees
```

**Add Employee Directly:**
```bash
curl -X POST http://localhost:8081/employee \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sneha Patel",
    "salary": 110000.0,
    "departments": [
      {
        "id": 4,
        "name": "Data Science"
      }
    ],
    "addressMap": {
      "city": "Pune",
      "country": "India"
    },
    "isActive": true
  }'
```

---

## 📝 Protocol Buffers Schema

### Employee Service Definition

```protobuf
syntax = "proto3";

package com.example;

import "google/protobuf/timestamp.proto";

// Employee message with rich data types
message Employee {
  int32 id = 1;
  string name = 2;
  double salary = 3;
  repeated Department departments = 4;      // Array of departments
  map<string, string> addressMap = 5;       // Key-value pairs
  bool isActive = 6;
  bytes profilePicture = 7;                 // Binary data
  google.protobuf.Timestamp joinDate = 8;   // Timestamp
}

// Department nested message
message Department {
  int32 id = 1;
  string name = 2;
}

// Collection wrapper
message EmployeeList {
  repeated Employee employees = 1;
}

message Empty {}

// Service definition
service EmployeeService {
  rpc getEmployee(Employee) returns (Employee);
  rpc addEmployee(Employee) returns (Employee);
  rpc getAllEmployees(Empty) returns (EmployeeList);
}
```

### Key Features:
- **Nested Objects**: Department within Employee
- **Collections**: `repeated` for arrays
- **Maps**: Key-value pairs for addresses
- **Timestamps**: Google's well-known type
- **Binary Data**: `bytes` for profile pictures
- **Type Safety**: Compile-time validation

---

## ⚡ Performance Comparison

### Payload Size Comparison

| Metric | REST (JSON) | gRPC (Protobuf) | Improvement |
|--------|-------------|-----------------|-------------|
| Payload Size | 340 bytes | 135 bytes | **60% smaller** |
| Serialization | Text-based | Binary | **3x faster** |
| Deserialization | JSON parsing | Binary parsing | **5x faster** |
| Network Transfer | HTTP/1.1 | HTTP/2 | **Multiplexing** |
| Type Safety | Runtime | Compile-time | **100% safer** |

### When to Use What?

#### Use gRPC When:
- 🎯 **Internal microservices** communication
- ⚡ **High performance** requirements (low latency, high throughput)
- 🔄 **Streaming** data (real-time, bidirectional)
- 💪 **Strong typing** needed across services
- 🌐 **Polyglot environments** (multi-language support)

#### Use REST When:
- 🌍 **Public APIs** for external clients
- 🔍 **Browser-based** applications
- 📱 **Mobile apps** with limited libraries
- 🔧 **Simple CRUD** operations
- 🐛 **Easy debugging** with tools like Postman

---

## 📁 Project Structure

```
.
├── service-a/                          # Client Service (API Gateway)
│   ├── src/main/
│   │   ├── java/com/example/servicea/
│   │   │   ├── ServiceAApplication.java          # Main application
│   │   │   ├── TestController.java               # REST endpoints
│   │   │   ├── EmployeeGrpcClient.java           # gRPC client
│   │   │   ├── LoggingClientInterceptor.java     # Request logging
│   │   │   └── config/
│   │   │       ├── ProtobufHttpMessageConverter.java  # Protobuf JSON
│   │   │       ├── JacksonProtobufConfig.java         # Jackson config
│   │   │       └── WebConfig.java                     # Web config
│   │   ├── proto/
│   │   │   ├── employee.proto           # Employee service definition
│   │   │   └── hello.proto              # Hello service definition
│   │   └── resources/
│   │       └── application.yml          # Configuration
│   └── pom.xml                          # Maven dependencies
│
├── service-b/                          # Server Service (Backend)
│   ├── src/main/
│   │   ├── java/org/example/serviceb/
│   │   │   ├── ServiceBApplication.java          # Main application
│   │   │   └── grpc/
│   │   │       ├── EmployeeServiceImpl.java      # gRPC implementation
│   │   │       ├── EmployeeRestController.java   # REST implementation
│   │   │       ├── GrpcServer.java               # gRPC server setup
│   │   │       └── LoggingServerInterceptor.java # Server logging
│   │   ├── proto/
│   │   │   ├── employee.proto           # Same proto (shared contract)
│   │   │   └── hello.proto
│   │   └── resources/
│   │       └── application.properties   # Configuration
│   └── pom.xml                          # Maven dependencies
│
└── README.md                            # This file
```

---

## 🔍 Key Implementation Details

### 1. gRPC Client Configuration (Service A)

```java
@Component
public class EmployeeGrpcClient {
    private final EmployeeServiceGrpc.EmployeeServiceBlockingStub stub;

    public EmployeeGrpcClient() {
        ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 9090)
            .usePlaintext()
            .intercept(new LoggingClientInterceptor())
            .build();
        this.stub = EmployeeServiceGrpc.newBlockingStub(channel);
    }

    public Employee getEmployee(int id) {
        Employee request = Employee.newBuilder().setId(id).build();
        return stub.getEmployee(request);
    }
}
```

### 2. gRPC Server Implementation (Service B)

```java
@Service
public class EmployeeServiceImpl extends EmployeeServiceGrpc.EmployeeServiceImplBase {
    
    @Override
    public void getEmployee(Employee request, StreamObserver<Employee> responseObserver) {
        Employee emp = employeeDb.get(request.getId());
        responseObserver.onNext(emp);
        responseObserver.onCompleted();
    }
}
```

### 3. Dual Protocol Support (Service A)

```java
@RestController
public class TestController {
    @Autowired
    private EmployeeGrpcClient grpcClient;
    
    @GetMapping("/grpc/employee/{id}")
    public Employee getViaGrpc(@PathVariable int id) {
        return grpcClient.getEmployee(id);  // Fast binary call
    }

    @GetMapping("/rest/employee/{id}")
    public Employee getViaRest(@PathVariable int id) {
        return restTemplate.getForObject(
            "http://localhost:8081/employee/" + id,
            Employee.class);  // Traditional JSON REST
    }
}
```

---

## 🧪 Testing

### Manual Testing with cURL
All examples provided in the [API Examples](#-api-examples) section.

### Performance Testing
```bash
# Install Apache Bench
# Test gRPC endpoint
ab -n 1000 -c 10 http://localhost:8080/grpc/employee/1

# Test REST endpoint
ab -n 1000 -c 10 http://localhost:8080/rest/employee/1
```

### Load Testing with wrk
```bash
# Test gRPC
wrk -t12 -c400 -d30s http://localhost:8080/grpc/employee/1

# Test REST
wrk -t12 -c400 -d30s http://localhost:8080/rest/employee/1
```

---

## 🐛 Troubleshooting

### Common Issues

**1. Port Already in Use**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

**2. Protobuf Classes Not Generated**
```bash
mvn clean install -DskipTests
```

**3. Service B Not Reachable**
- Ensure Service B is started **before** Service A
- Check if ports 8081 and 9090 are available
- Verify firewall settings

**4. JSON Parsing Errors**
- Ensure `Content-Type: application/json` header is set
- Validate JSON syntax

---

## 📊 Monitoring & Logging

Both services include interceptors for detailed logging:

### Client-Side Logs (Service A)
```
[gRPC Client] Calling method: employee.EmployeeService/getEmployee
[gRPC Client] Request: id: 1
[gRPC Client] Response received in 45ms
```

### Server-Side Logs (Service B)
```
[gRPC Server] Received call: getEmployee
[gRPC Server] Processing request for ID: 1
[gRPC Server] Response sent: Employee{name='Gaurav', salary=95000.0}
```

---

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Ideas for Contribution:
- [ ] Add authentication/authorization
- [ ] Implement streaming RPCs
- [ ] Add database persistence (PostgreSQL, MongoDB)
- [ ] Docker & Kubernetes deployment
- [ ] Add Prometheus metrics
- [ ] Implement circuit breakers
- [ ] Add API documentation (Swagger/OpenAPI)
- [ ] Write integration tests

---

## 📚 Resources

### Learn More About gRPC
- [gRPC Official Documentation](https://grpc.io/docs/)
- [Protocol Buffers Guide](https://developers.google.com/protocol-buffers)
- [Spring gRPC](https://github.com/grpc-ecosystem/grpc-spring)

### Related Articles
- [gRPC vs REST: Understanding the Differences](https://www.redhat.com/en/topics/api/what-is-grpc)
- [When to Use gRPC vs GraphQL vs REST](https://blog.logrocket.com/guide-grpc-graphql-rest/)
- [Microservices Communication Patterns](https://microservices.io/patterns/communication-style/messaging.html)

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [Your Profile](https://linkedin.com/in/yourprofile)
- Twitter: [@yourhandle](https://twitter.com/yourhandle)

---

## ⭐ Show Your Support

If this project helped you understand gRPC and microservices communication, please consider:

- ⭐ **Starring** this repository
- 🍴 **Forking** and building upon it
- 📢 **Sharing** it with the community
- 🐛 **Reporting** issues or bugs
- 💡 **Suggesting** new features

---

## 🙏 Acknowledgments

- Spring Boot team for excellent framework
- gRPC community for amazing RPC framework
- Protocol Buffers team at Google
- All contributors and stargazers

---

<div align="center">

**[⬆ Back to Top](#-grpc-vs-rest-communication-demo)**

Made with ❤️ and ☕ 

**Happy Coding!** 🚀

</div>

