# Usar la imagen base de OpenJDK 17
FROM openjdk:17

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app/msvc-usuarios

# Copiar los archivos de Maven necesarios para descargar las dependencias
COPY ./pom.xml /app
COPY ./msvc-usuarios/.mvn ./.mvn
COPY ./msvc-usuarios/mvnw .
COPY ./msvc-usuarios/pom.xml .

# 5. Ejecutar el primer comando de Maven para construir la aplicación
#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

# Descargar las dependencias y almacenarlas en caché
RUN ./mvnw dependency:go-offline

# Copiar el código fuente al contenedor
COPY ./msvc-usuarios/src ./src

# Construir el proyecto y empaquetar el JAR
RUN ./mvnw clean package -DskipTests

# Exponer el puerto para la aplicación
EXPOSE 8001

# Definir el comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "./target/msvc-usuarios-0.0.1-SNAPSHOT.jar"]
