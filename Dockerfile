FROM maven:3.9.6-eclipse-temurin-21
WORKDIR /app

# Instalar curl para health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copiar archivos del proyecto
COPY pom.xml .
COPY src ./src

# Ejecutar la aplicación con Maven
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=docker"]