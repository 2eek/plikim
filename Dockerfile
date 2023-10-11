#FROM openjdk:11
#ARG JAR_FILE=target/*.jar
#WORKDIR /app
#COPY ${JAR_FILE} /app/app.jar
#CMD ["java","-jar", "app.jar"]


FROM openjdk:11
VOLUME /tmp
#target안에 위치한 kimpli-0.0.1-SNAPSHOT.jar 이걸 이미지화 하겠다!!
ARG JAR_FILE=target/kimpli-0.0.1-SNAPSHOT.jar
#실제로 빌드된 JAR 파일을 Docker 이미지 안으로 복사
ADD ${JAR_FILE} app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-jar","/app.jar"]