FROM maven:3.8.3-openjdk-17-slim AS build

WORKDIR /src
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-slim
RUN addgroup --system app && adduser --system --group app
USER app
WORKDIR /app
COPY --from=build /src/target/*.jar /app/processor.jar
EXPOSE 8081
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:MaxMetaspaceSize=256m -XX:+UseG1GC -XX:+UseStringDeduplication -XX:+ExitOnOutOfMemoryError"
ENTRYPOINT ["java", "-jar", "/app/processor.jar"]

