# 這個檔案是用來建置和執行訂單服務的 Docker 映像檔。

# ====== 第一階段：建置 ======
FROM maven:3.9.5-eclipse-temurin-17 as build
# 使用 Maven 的基底映像檔，內建 Java 17 和 Maven 3.9.5

WORKDIR /app
# 設定工作目錄為 /app

COPY . .
# 把專案的檔案複製到容器裡的 /app 資料夾

RUN mvn clean package -DskipTests
# 用 Maven 打包專案，跳過測試

# ====== 第二階段：執行 ======
FROM openjdk:17-jdk-slim
# 使用輕量版的 OpenJDK 17 基底映像檔

WORKDIR /app
# 設定工作目錄為 /app

COPY --from=build /app/target/*.jar app.jar
# 從第一階段複製打包好的 JAR 檔案

EXPOSE 8082
# 開放 8082 埠號，讓外部可以連進來

ENTRYPOINT ["java", "-jar", "app.jar"]
# 設定啟動指令，執行 JAR 檔案