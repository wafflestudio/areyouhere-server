FROM openjdk:17-jdk-slim

ENV SPRING_PROFILE=dev
WORKDIR /app
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]