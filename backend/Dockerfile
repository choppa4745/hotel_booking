FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Build argument to skip dependency download step
ARG SKIP_DEPENDENCY_DOWNLOAD=false

# Copy Maven configuration
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies with retry and increased timeouts
# Skip if SKIP_DEPENDENCY_DOWNLOAD is set to true
RUN if [ "$SKIP_DEPENDENCY_DOWNLOAD" != "true" ]; then \
        mvn dependency:resolve -B \
            -Dmaven.wagon.http.retryHandler.count=3 \
            -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 \
            -Dmaven.wagon.http.readTimeout=60000 \
            || mvn dependency:resolve -B \
            -Dmaven.wagon.http.retryHandler.count=5 \
            -Dmaven.wagon.httpconnectionManager.ttlSeconds=180 \
            -Dmaven.wagon.http.readTimeout=90000; \
    fi

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

