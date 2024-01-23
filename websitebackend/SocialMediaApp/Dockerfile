# Stage 1: Build the application
FROM maven:3.6-jdk-11 as builder
COPY . .
RUN mvn package

# Stage 2: Setup the runtime environment
FROM eclipse-temurin:17-jre
COPY --from=builder /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]