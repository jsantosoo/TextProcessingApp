# Use a Gradle image that supports Java 21
FROM gradle:8-jdk AS build

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper and build files
COPY ./gradlew ./gradlew
COPY ./gradle/wrapper ./gradle/wrapper
COPY ./build.gradle ./build.gradle
COPY ./settings.gradle ./settings.gradle

# Copy the source code
COPY ./src ./src

# Build the application using the Gradle wrapper
RUN ./gradlew clean build -x test

# Use a minimal JRE image for the final container
FROM eclipse-temurin:21-jre

# Set timezone to UTC to avoid time-related issues
RUN ln -fs /usr/share/zoneinfo/UTC /etc/localtime && dpkg-reconfigure -f noninteractive tzdata

# Install CA certificates to fix SSL issues
RUN apt-get update && apt-get install -y ca-certificates && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/build/libs/TextProcessingApp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
