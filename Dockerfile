# Use Java 21 because the Spring Boot project targets Java 21.
FROM eclipse-temurin:21-jdk

# Set the working directory inside the container.
WORKDIR /app

# Copy the Maven project files.
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Copy the application source code.
COPY src src

# Make the Maven wrapper executable.
RUN chmod +x mvnw

# Build the Spring Boot application.
RUN ./mvnw clean package -DskipTests

# Render provides the PORT environment variable.
EXPOSE 8080

# Start the generated Spring Boot JAR.
CMD ["sh", "-c", "java -jar target/*.jar"]