FROM maven:3.8.3-openjdk-17-slim AS build

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract && \
    rm application.jar

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build dependencies/ ./
COPY --from=build snapshot-dependencies/ ./
COPY --from=build spring-boot-loader/ ./
COPY --from=build application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]


