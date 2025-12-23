FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven configuration
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Copy source code
COPY src ./src

# Build with detailed logging and retry settings
# Maven will download dependencies automatically during build
RUN echo "=== Starting Maven build ===" && \
    echo "Maven version: $(mvn -version)" && \
    echo "Java version: $(java -version 2>&1)" && \
    mvn clean package -DskipTests -B -e \
        -Dmaven.wagon.http.retryHandler.count=5 \
        -Dmaven.wagon.httpconnectionManager.ttlSeconds=180 \
        -Dmaven.wagon.http.readTimeout=120000 \
        -Dmaven.wagon.http.pool.timeout=120000 \
        -Dmaven.wagon.http.socket.timeout=120000 || \
    (echo "=== First build attempt failed, retrying with extended timeouts ===" && \
     echo "Error details above. Retrying..." && \
     mvn clean package -DskipTests -B -e \
         -Dmaven.wagon.http.retryHandler.count=10 \
         -Dmaven.wagon.httpconnectionManager.ttlSeconds=300 \
         -Dmaven.wagon.http.readTimeout=180000 \
         -Dmaven.wagon.http.pool.timeout=180000 \
         -Dmaven.wagon.http.socket.timeout=180000)

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

