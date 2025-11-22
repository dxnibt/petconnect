# Imagen base con Java 21
FROM eclipse-temurin:21-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

COPY target/app.jar app.jar

# Exponer el puerto de la app
EXPOSE 8181

# Ejecutar la app usando la variable de entorno JWT_SECRET
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
