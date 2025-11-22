FROM eclipse-temurin:21-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8181


# Comando para arrancar la aplicación usando el JAR generado
CMD ["java", "-jar", "target/auth-0.0.1-SNAPSHOT"]
