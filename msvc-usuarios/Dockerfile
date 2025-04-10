# Imagen base de Amazon Corretto 17 (Java 17 en Alpine)
FROM amazoncorretto:17-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo .jar de la aplicación
COPY target/msvc-usuarios-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto de la aplicación
EXPOSE 8080

# Ejecutar la aplicación con Java
ENTRYPOINT ["java", "-jar", "app.jar"]
