LABEL authors="NixonChebii"
# Build stage
FROM maven:3.8.3-openjdk-17 AS builder

LABEL maintainer="nicchebii@gmail.com"

COPY ./ /app/
# Setting up work directory
WORKDIR /app/

RUN mvn clean install -DskipTests

# Fetching latest version of Java #Production stage
FROM openjdk:17-jdk-slim AS prod

WORKDIR /app/
# Copy the jar file into our app -- cardserver-0.0.1.jar
COPY --from=builder /app/target/*.jar ./cardserver.jar

# Exposing port 8080
EXPOSE 8080

# Starting the application
ENTRYPOINT  ["java", "-jar", "cardserver.jar"]