FROM openjdk:11
#COPY target/kimpli-0.0.1-SNAPSHOT.jar /app.jar
COPY --from=builder target/*.jar app.jar
CMD ["java","-jar", "app.jar"]