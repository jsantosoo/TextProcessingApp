# TextRepositoryApp

TextRepositoryApp is a Java-based application designed to process and manage text data, leveraging Kafka for asynchronous communication. The project includes API documentation (OpenAPI and AsyncAPI) and is containerized for easy deployment.

## Features
- Processes text data and computes statistics (e.g., most frequent word, average paragraph size)
- Consumes computation results from a Kafka topic (`computationResult`)
- REST API endpoints (see `openapi.yaml`)
- AsyncAPI documentation for Kafka topics (`asyncapi.yaml`)
- Docker and Docker Compose support

## Project Structure
- `src/main/java` - Main application source code
- `src/test/java` - Unit and integration tests
- `src/main/resources` - Application configuration and static resources
- `openapi.yaml` - REST API documentation
- `asyncapi.yaml` - Kafka topics documentation
- `Dockerfile` - Containerization setup
- `docker-compose.yml` - Multi-container orchestration

## Getting Started

### Prerequisites
- Java 17 or higher
- Gradle
- Docker (optional, for containerized deployment)
- Kafka (can be started via Docker Compose)

### Build and Run

#### Using Gradle
```sh
./gradlew build
./gradlew bootRun
```

#### Using Docker Compose
```sh
docker-compose up --build
```

### API Documentation
- REST API: See `openapi.yaml`
- Kafka Topics: See `asyncapi.yaml`

## License
This project is provided for demonstration purposes.

