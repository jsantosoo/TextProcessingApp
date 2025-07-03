# Text Processing Workspace

This workspace contains two Java-based microservices designed for text data processing and management, leveraging Kafka for asynchronous communication. Both services are containerized and include API documentation for easy integration and deployment.

## Projects

### 1. TextRepositoryApp
- Manages and stores text data.
- Consumes computation results from Kafka (`computationResult` topic).
- Provides REST API endpoints (see `openapi.yaml`).
- AsyncAPI documentation for Kafka topics (`asyncapi.yaml`).
- Located in: `TextRepositoryApp/`

### 2. TextProcessingApp
- Processes text data and computes statistics (e.g., most frequent word, average paragraph size).
- Produces computation results to Kafka.
- Provides REST API endpoints (see `openapi.yaml`).
- AsyncAPI documentation for Kafka topics (`asyncapi.yaml`).
- Located in: `TextProcessingApp/`

## Project Structure
- `TextRepositoryApp/` - Text repository microservice
- `TextProcessingApp/` - Text processing microservice
- `docker-compose.yml` - Multi-container orchestration for both services and Kafka

## Prerequisites
- A container management service (e.g., Docker, Podman)

## Getting Started

### Build and Run All Services

#### Using start.sh (Recommended)
The provided script uses Podman as the container management service by default. If you prefer to use Docker, you will need to modify the script accordingly.

Run the script to build and start all services:
```sh
chmod +x start.sh && ./start.sh
```

## API Documentation
- Each service provides OpenAPI (`openapi.yaml`) and AsyncAPI (`asyncapi.yaml`) documentation in its directory.
- After starting the services, you can visualize the OpenAPI documentation at:
  - TextProcessingApp: http://localhost:8080/swagger-ui.html
  - TextRepositoryApp: http://localhost:8082/swagger-ui.html
- To visualize Kafka messages, open: http://localhost:8083
- To access MongoDB, open: http://localhost:8081

## License
This workspace is provided for demonstration purposes.
