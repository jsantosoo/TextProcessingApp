# Text Processing App

This project is a Text Processing application that leverages Kafka for messaging and is built with Java (Spring Boot). It processes text and publishes the results to a Kafka topic. The project includes an AsyncAPI specification for the Kafka producer and an OpenAPI specification for the REST API.

## Features
- REST API for submitting text to be processed
- Kafka producer for publishing processed text results
- AsyncAPI documentation for Kafka integration
- OpenAPI documentation for REST endpoints

## Project Structure
- `src/main/java/de/textprocessingapp/` - Java source code
- `src/main/resources/` - Application resources
- `asyncapi.yaml` - AsyncAPI specification for Kafka
- `openapi.yaml` - OpenAPI specification for REST API
- `build.gradle` - Gradle build file
- `docker-compose.yml` - Docker Compose for running dependencies (e.g., Kafka)

## Prerequisites
- Java 17 or later
- Gradle
- Docker (for running Kafka via Docker Compose)

## Getting Started

### 1. Clone the repository
```sh
git clone <repository-url>
cd TextProcessingApp
```

### 2. Start Kafka (using Docker Compose)
```sh
docker-compose up -d
```

### 3. Build the application
```sh
./gradlew build
```

### 4. Run the application
```sh
./gradlew bootRun
```

## API Documentation
- OpenAPI: See `openapi.yaml` for REST API documentation.
- AsyncAPI: See `asyncapi.yaml` for Kafka producer documentation.

## Usage
- Submit text to the REST API endpoint (see OpenAPI docs for details).
- Processed results are published to the Kafka topic `words.processed`.

## License
This project is licensed under the MIT License.

