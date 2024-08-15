# Use the official Eclipse Temurin image with JDK 21 to build the application
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven

# Copy the pom.xml and install dependencies (cached)
COPY pom.xml .
# COPY .env .
RUN mvn dependency:go-offline -B

# Copy the application source code
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use the same Eclipse Temurin image to run the application
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar myapp.jar
# COPY --from=build /app/.env .env
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "myapp.jar"]
