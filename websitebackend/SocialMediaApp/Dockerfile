# Use a base image with Maven and JDK installed
FROM maven:3.6-jdk-11 as builder

# Copy your source code into the image
COPY . /app
WORKDIR /app

# Build the application
RUN mvn package

# Use a base image for the final image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/SocialMediaApp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]