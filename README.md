# DistriSchool API Gateway

A Spring Cloud Gateway service that acts as the central entry point for the DistriSchool microservices architecture, providing routing, load balancing, and CORS handling capabilities.

## Overview

The API Gateway serves as a single entry point for all client requests, routing them to the appropriate microservices while providing cross-cutting concerns like CORS handling, request logging, and health monitoring.

## Features

- **Service Routing**: Routes requests to appropriate microservices based on URL patterns
- **CORS Handling**: Configurable CORS support for frontend applications
- **Health Monitoring**: Built-in health checks and metrics via Spring Boot Actuator
- **Request/Response Logging**: Comprehensive logging for debugging and monitoring
- **Load Balancing**: Built-in load balancing capabilities
- **Security**: Request header manipulation and security filters

## Architecture

```
Client Request → API Gateway → Microservice
     ↓              ↓              ↓
Frontend    →  Route Config  →  Auth Service
             →  CORS Config  →  Student Service
             →  Logging      →  Teacher Service
```

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Cloud Gateway 2023.0.0**
- **Spring WebFlux** (Reactive Programming)
- **Maven** (Build Tool)

## Service Routes

The gateway routes requests to the following microservices:

| Route Pattern | Target Service | Description |
|---------------|----------------|-------------|
| `/api/auth/**` | Auth Service | Authentication and authorization |
| `/api/students/**` | Student Management Service | Student data management |
| `/api/teachers/**` | Teacher Management Service | Teacher data management |

## Configuration

### Environment Variables

- `SERVER_PORT`: Port for the gateway service (default: 8080)
- `SPRING_PROFILES_ACTIVE`: Active Spring profile (dev/prod)

### CORS Configuration

The gateway is configured to allow requests from:
- `http://localhost:3000` (Next.js development server)
- `http://localhost:5173` (Vite development server)

### Route Configuration

Routes are configured in `src/main/resources/application.yml` and can be overridden via environment variables for different deployment environments.

## Development

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Docker (optional, for containerized development)

### Local Development

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd distrischool-api-gateway
   ```

2. **Build the application**:
   ```bash
   mvn clean install
   ```

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**:
   - Gateway: http://localhost:8080
   - Health Check: http://localhost:8080/actuator/health
   - Gateway Routes: http://localhost:8080/actuator/gateway/routes

### Docker Development

1. **Build the Docker image**:
   ```bash
   docker build -t distrischool-api-gateway .
   ```

2. **Run the container**:
   ```bash
   docker run -p 8080:8080 distrischool-api-gateway
   ```

## Deployment

### Docker Compose Integration

When used with the main DistriSchool project, the API Gateway can be integrated via Docker Compose:

```yaml
api-gateway:
  build:
    context: ./distrischool-api-gateway
    dockerfile: Dockerfile
  ports:
    - "8080:8080"
  environment:
    SERVER_PORT: 8080
    SPRING_PROFILES_ACTIVE: dev
  networks:
    - distrischool-network
```

### Kubernetes Deployment

For production deployments, the service can be deployed to Kubernetes with appropriate service and ingress configurations.

## Monitoring and Health Checks

The API Gateway exposes several monitoring endpoints:

- `/actuator/health` - Application health status
- `/actuator/info` - Application information
- `/actuator/metrics` - Application metrics
- `/actuator/gateway/routes` - Gateway route information

## Logging

The application uses structured logging with different levels:

- `DEBUG`: Gateway routing details
- `INFO`: General application information
- `WARN`: Warning messages
- `ERROR`: Error messages

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is part of the DistriSchool system and follows the same licensing terms.

## Support

For issues and questions, please refer to the main DistriSchool project documentation or create an issue in the repository.

