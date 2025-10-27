# Этап 1: Сборка
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Копируем pom.xml для загрузки зависимостей
COPY pom.xml .

# Загружаем зависимости оффлайн
RUN mvn dependency:go-offline -B

# Добавляем папку с jar-файлом в образ
COPY libs ./libs
COPY src ./src

# Теперь добавляем команду установки локального jar
RUN mvn install:install-file \
    -Dfile=libs/hf-tcp-gateway-1-0-0.jar \
    -DgroupId=com.hfims.boot \
    -DartifactId=hf-tcp-gateway \
    -Dversion=1.0.0 \
    -Dpackaging=jar

# И только потом собираем проект
RUN mvn clean package -DskipTests

# Этап 2: Запуск (лёгкий runtime)
FROM eclipse-temurin:21-jre

WORKDIR /app

# Копируем собранный jar-файл из этапа сборки
COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
