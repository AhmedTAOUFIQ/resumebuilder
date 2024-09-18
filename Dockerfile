FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/resumebuilder-0.0.1-SNAPSHOT.jar  /app/resumebuilder.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "resumebuilder.jar"]
