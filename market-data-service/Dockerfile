# ====== Stage 1: Build ======
FROM maven:3.9.5-eclipse-temurin-17 as build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

# ====== Stage 2: Run ======
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]