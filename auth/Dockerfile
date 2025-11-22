# Usar JDK 21 de Temurin (Alpine para ser liviano)
FROM eclipse-temurin:21-jdk-alpine

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copiar pom.xml y bajar dependencias para aprovechar caching
COPY pom.xml .
RUN ./mvnw dependency:go-offline -B

# Copiar todo el proyecto al contenedor
COPY . .

# Construir el JAR del proyecto, omitiendo tests para acelerar
RUN ./mvnw clean package -DskipTests

# Render asigna un puerto dinámicamente mediante la variable PORT
ENV PORT=8181
EXPOSE $PORT

# Comando para arrancar la aplicación usando el JAR generado
CMD ["java", "-jar", "target/auth-0.0.1-SNAPSHOT"]
