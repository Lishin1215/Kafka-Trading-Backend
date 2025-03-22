# 此檔案用於構建用戶服務的 Docker 映像檔，分為構建階段與運行階段。

# ====== Stage 1: Build ======
FROM maven:3.9.5-eclipse-temurin-17 as build 
# 使用 Maven 基底映像檔

WORKDIR /app 
# 設定工作目錄

COPY . . 
# 複製專案檔案到容器

RUN mvn clean package -DskipTests 
# 使用 Maven 打包專案，跳過測試

# ====== Stage 2: Run ======
FROM openjdk:17-jdk-slim 
# 使用 OpenJDK 基底映像檔

WORKDIR /app 
# 設定工作目錄

COPY --from=build /app/target/*.jar app.jar 
# 從構建階段複製打包好的 JAR 檔案

EXPOSE 8081 
# 開放埠號 8081

ENTRYPOINT ["java", "-jar", "app.jar"] 
# 設定容器啟動指令