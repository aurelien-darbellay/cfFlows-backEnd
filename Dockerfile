# ============================
# ---- Stage 1: Builder ----
# ============================
FROM gradle:8.4-jdk17-alpine AS builder

WORKDIR /app

# Copy Gradle wrapper and configuration *first* for caching
COPY gradlew settings.gradle build.gradle /app/
COPY gradle /app/gradle

# Ensure wrapper is executable
RUN chmod +x /app/gradlew

# Pre-download dependencies
RUN ./gradlew --no-daemon build -x test || true

# Now copy *all* sources
COPY . /app

# Ensure wrapper is executable AGAIN (in case COPY overwrote it)
RUN chmod +x /app/gradlew

# Build the fat jar
RUN ./gradlew --no-daemon clean bootJar

# ============================
# ---- Stage 2: Runner ----
# ============================
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
