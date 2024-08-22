# Usar la imagen base de OpenJDK 17
FROM openjdk:17-jdk-alpine as builder

# Argumento
ARG MSVC_U=msvc-usuarios

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app/$MSVC_U

# Copiar los archivos de Maven necesarios para descargar las dependencias
# Copiar pom.xml del padre ---> /app/pom.xml
COPY ./pom.xml /app
# Copiar de msvc-usuarios el .mvn a ---> /app/msvc-usuarios/.mvn/
COPY ./$MSVC_U/.mvn ./.mvn
# Copiar de msvc-usuarios el mvnw a ---> /app/msvc-usuarios/mvnw
COPY ./$MSVC_U/mvnw .
# Copiar de msvc-usuarios el pom.xml a ---> /app/msvc-usuarios/pom.xml
COPY ./$MSVC_U/pom.xml .

# 5. Ejecutar el primer comando de Maven para construir la aplicacion
#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

# Descargar las dependencias y almacenarlas en cache
RUN ./mvnw dependency:go-offline

# Copiar el codigo fuente al contenedor
# Copiar de msvc-usuarios el src a ---> /app/msvc-usuarios/src/
COPY ./$MSVC_U/src ./src

# Construir el proyecto y empaquetar el JAR
RUN ./mvnw clean package -DskipTests

# Inicia una nueva imagen base para la fase final, que sera la imagen resultante.
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo en /app.
WORKDIR /app

# Copia el archivo jar generado en la fase de construcción (builder) al directorio /app de la imagen final.
COPY --from=builder /app/msvc-usuarios/target/msvc-usuarios-0.0.1-SNAPSHOT.jar ./app.jar

ENV PORT 8001

# Exponer el puerto para la aplicacion
EXPOSE $PORT

# Define el comando de inicio para el contenedor, que ejecutara la aplicacion Java.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
