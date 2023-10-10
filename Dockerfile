#FROM openjdk:11
#ARG JAR_FILE=target/*.jar
#WORKDIR /app
#COPY ${JAR_FILE} /app/app.jar
#CMD ["java","-jar", "app.jar"]


FROM openjdk:11
VOLUME /tmp
ARG JAR_FILE=jenkins-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-jar","/app.jar"]