# ============================
# ---- Stage 1: Build JAR ----
# ============================
FROM gradle:8.4-jdk17-alpine AS builder

WORKDIR /app

# Copy Gradle wrapper and settings first
COPY settings.gradle build.gradle gradlew /app/
COPY gradle /app/gradle

# Ensure gradlew is executable
RUN chmod +x gradlew

# Pre-download dependencies
RUN ./gradlew --no-daemon build -x test || return 0

# Now copy the actual source code
COPY . /app

# Build the jar
RUN ./gradlew --no-daemon clean bootJar

# =============================
# ---- Stage 2: Run JAR ----
# =============================
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
