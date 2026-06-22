# Paso 1: Compilar la aplicación con Maven y Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# Paso 2: Correr la aplicación
FROM eclipse-temurin:21-jre-jammy
COPY --from=build /target/clinica-0.0.1-SNAPSHOT.jar clinica.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "clinica.jar"]
