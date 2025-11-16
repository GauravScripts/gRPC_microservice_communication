# Docker Quick Start Guide

## 🚀 Running the Services

### Start Everything
```bash
docker-compose up --build
```

### Start in Background (Detached Mode)
```bash
docker-compose up -d --build
```

### Stop Services
```bash
docker-compose down
```

### Stop and Remove Volumes
```bash
docker-compose down -v
```

## 📊 Monitoring

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f service-a
docker-compose logs -f service-b
```

### Check Service Status
```bash
docker-compose ps
```

### Execute Commands in Running Container
```bash
docker-compose exec service-a bash
docker-compose exec service-b bash
```

## 🧹 Cleanup

### Remove All Containers
```bash
docker-compose down
```

### Remove All Images
```bash
docker-compose down --rmi all
```

### Clean Docker System
```bash
# Remove unused data
docker system prune

# Remove everything (including volumes)
docker system prune -a --volumes
```

## 🔧 Rebuild After Code Changes

### Rebuild All Services
```bash
docker-compose build --no-cache
docker-compose up
```

### Rebuild Specific Service
```bash
docker-compose build --no-cache service-a
docker-compose up
```

## 🧪 Testing the Services

### Test Service A
```bash
curl http://localhost:8080/api/hello
```

### Test Service B
```bash
curl http://localhost:8081/health
```

### Test gRPC Communication
Service A will automatically connect to Service B via gRPC when you call its endpoints.

## 📝 Configuration

### Ports
- **Service A**: 8080 (HTTP/REST)
- **Service B**: 8081 (HTTP/REST), 9090 (gRPC)

### Network
Both services communicate on the `grpc-network` bridge network.

## ⚠️ Troubleshooting

### Port Already in Use
```bash
# Find process using port
netstat -ano | findstr :8080
netstat -ano | findstr :8081
netstat -ano | findstr :9090

# Kill process by PID
taskkill /PID <PID> /F
```

### Container Won't Start
```bash
# View detailed logs
docker-compose logs service-a
docker-compose logs service-b

# Restart specific service
docker-compose restart service-a
```

### Connection Issues
```bash
# Check if services are in the same network
docker network inspect grpc-network

# Ping between containers
docker-compose exec service-a ping service-b
```

## 🎯 Quick Commands Cheat Sheet

| Command | Description |
|---------|-------------|
| `docker-compose up` | Start services |
| `docker-compose up -d` | Start in background |
| `docker-compose down` | Stop services |
| `docker-compose logs -f` | Follow logs |
| `docker-compose ps` | List containers |
| `docker-compose restart` | Restart services |
| `docker-compose build` | Rebuild images |
| `docker-compose exec <service> bash` | Enter container |

## 🔄 Development Workflow

1. Make code changes
2. Rebuild the service:
   ```bash
   docker-compose build service-a
   ```
3. Restart the service:
   ```bash
   docker-compose up -d service-a
   ```
4. Check logs:
   ```bash
   docker-compose logs -f service-a
   ```

That's it! Your services are now fully containerized and ready to run anywhere Docker is available. 🎉

