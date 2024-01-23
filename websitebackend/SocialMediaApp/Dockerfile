
FROM maven:3.6-jdk-11 as builder
COPY . .

RUN mvn -e -X package


FROM eclipse-temurin:17-jre
COPY --from=builder /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]