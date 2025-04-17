FROM eclipse-temurin:17-jdk-alpine

EXPOSE 8081

WORKDIR /app
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]


