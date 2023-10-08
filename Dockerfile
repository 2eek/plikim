FROM openjdk:11
#COPY /target/kimpli-0.0.1-SNAPSHOT.jar /app.jar
ARG JAR_FILE=target/*.jar
WORKDIR /app
COPY ${JAR_FILE} /app/app.jar
CMD ["java","-jar", "app.jar"]
