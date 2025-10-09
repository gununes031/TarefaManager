# Use imagem oficial do Maven para build
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o pom.xml e o diretório src para dentro do container
COPY pom.xml .
COPY src ./src

# Build do projeto, empacota em um jar
RUN mvn clean package -DskipTests

# Segunda fase: imagem mais leve para rodar o aplicativo
FROM eclipse-temurin:21-jdk-alpine

# Diretório de trabalho
WORKDIR /app

# Copia o jar gerado da fase de build
COPY --from=build /app/target/*.jar app.jar

# Porta que a aplicação irá expor
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
