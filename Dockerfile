FROM ubuntu:latest

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . /app

RUN apt-get install maven -y
RUN cd app && mvn clean install -DskipTests

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/app/target/api-test-user-0.0.1-SNAPSHOT.jar"]