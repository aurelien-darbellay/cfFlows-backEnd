# Start from a minimal JVM base image
FROM eclipse-temurin:17-jdk-alpine

# Set workdir
WORKDIR /app

# Copy the jar into the container
COPY build/libs/interactiveCV-0.0.1-SNAPSHOT.jar app.jar

# Expose the app port
EXPOSE 8080

# Run it
ENTRYPOINT ["java","-jar","/app/app.jar"]
