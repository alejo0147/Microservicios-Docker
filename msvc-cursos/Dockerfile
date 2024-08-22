# Usar la imagen base de OpenJDK 17
FROM openjdk:17-jdk-alpine as builder

# Argumento
ARG MSVC_C=msvc-cursos

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app/$MSVC_C

# Copiar los archivos de Maven necesarios para descargar las dependencias
# Copiar pom.xml del padre ---> /app/pom.xml
COPY ./pom.xml /app
# Copiar de msvc-cursos el .mvn a ---> /app/msvc-cursos/.mvn/
COPY ./$MSVC_C/.mvn ./.mvn
# Copiar de msvc-cursos el mvnw a ---> /app/msvc-cursos/mvnw
COPY ./$MSVC_C/mvnw .
# Copiar de msvc-cursos el pom.xml a ---> /app/msvc-cursos/pom.xml
COPY ./$MSVC_C/pom.xml .

# 5. Ejecutar el primer comando de Maven para construir la aplicacion
#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

# Descargar las dependencias y almacenarlas en cache
RUN ./mvnw dependency:go-offline

# Copiar el codigo fuente al contenedor
# Copiar de msvc-cursos el src a ---> /app/msvc-cursos/src/
COPY ./$MSVC_C/src ./src

# Construir el proyecto y empaquetar el JAR
RUN ./mvnw clean package -DskipTests

# Inicia una nueva imagen base para la fase final, que sera la imagen resultante.
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo en /app.
WORKDIR /app

# Copia el archivo jar generado en la fase de construcción (builder) al directorio /app de la imagen final.
COPY --from=builder /app/msvc-cursos/target/msvc-cursos-0.0.1-SNAPSHOT.jar ./app.jar

ENV PORT 8002

# Exponer el puerto para la aplicacion
EXPOSE $PORT

# Define el comando de inicio para el contenedor, que ejecutara la aplicacion Java.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
