<<<<<<< HEAD
FROM eclipse-temurin:21-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8181


# Comando para arrancar la aplicación usando el JAR generado
CMD ["java", "-jar", "target/auth-0.0.1-SNAPSHOT"]
=======
# Imagen base con Java 21
FROM eclipse-temurin:21-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

COPY target/app.jar app.jar

# Exponer el puerto de la app
EXPOSE 8181

# Ejecutar la app usando la variable de entorno JWT_SECRET
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
>>>>>>> 9792caaa398d6fa8a5907924230055a76da4c622
