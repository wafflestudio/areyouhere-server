FROM openjdk:17-jdk-slim

WORKDIR /app
COPY build/libs/are-you-here.jar are-you-here.jar
ENTRYPOINT ["java", "-jar", "/app/are-you-here.jar"]
